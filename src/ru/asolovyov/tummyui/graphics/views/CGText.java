/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CG.MultilineText;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;
import ru.asolovyov.tummyui.graphics.CGSize;

/**
 *
 * @author Администратор
 */
public class CGText extends CGSomeDrawable implements CGFontSupporting {
    private Str text = new Str("");
    private Obj font = new Obj(Font.getDefaultFont());
    private Int anchor = new Int(CG.TOP | CG.LEFT);

    public CGText(Str text) {
        super();
        //TODO подписаться на остальное
        this.text(text);
        this.font(font);
    }
    
    public CGText text(Str text) {
        this.text = text;
        this.text.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                S.debugln("CGTEXT TEXT: " + value);
                updateIntrinsicContentSize();
            }
        });
        int inset = this.getFont().getHeight() / 4;
        this.contentInset(inset / 2, inset, inset / 2, inset);

        this.flexibility(new int[]{ CGDrawable.FLEXIBILITY_DEFAULT, CGDrawable.FLEXIBILITY_LOW });

        return this;
    }

    public CGText text(String text) {
        S.debugln("CGTEXT SET TEXT: " + text);
        this.text.setString(text);
        return this;
    }

    public CGText alignment(int anchor) {
        return this.alignment(new Int(anchor));
    }
    
    public CGText alignment(Int anchor) {
        anchor.route(this.anchor);
        return this;
    }

    private int getAlignment() {
        return this.anchor.getInt();
    }

    public Str text() {
        return this.text;
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        if (frame == null) {
            return;
        }
        
        g.setFont(getFont());
        g.setColor(this.color());

        CGInsets contentInset = contentInset();

        String text = this.text.getString();
        Font font = this.getFont();
        int anchor = this.getAlignment();

        MultilineText multilineText = CG.makeMultilineText(text, font, frame.getCGSize());
        
        int lineHeight = font.getHeight();
        for (int i = 0; i < multilineText.lines.size(); i++) {
            String line = (String) multilineText.lines.elementAt(i);
            int lineWidth = font.stringWidth(line);
            int textY = frame.y + i * lineHeight;
            int textX = frame.x + contentInset.left;

            if (CG.isBitSet(anchor, CG.VCENTER)) {
                textY += (frame.height - multilineText.height) / 2 + contentInset.top - contentInset.bottom;
            } else if (CG.isBitSet(anchor, CG.BOTTOM)) {
                textY += (frame.height - multilineText.height);
            }

            if (CG.isBitSet(anchor, CG.HCENTER)) {
                textX += (frame.width - lineWidth) / 2 - contentInset.right;
            } else if (CG.isBitSet(anchor, CG.RIGHT)) {
                textX += (frame.width - lineWidth);
            }
            
            g.drawString(line, textX, textY, 0);
        }
    }

    public CGFontSupporting font(Font font) {
        return this.font(new Obj(font));
    }

    public CGFontSupporting font(Obj font) {
        this.font = font;
        this.font.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                relayout();
            }
        });
        return this;
    }

    private Font getFont() {
        return (Font)this.font.getObject();
    }

    public CGDrawable sizeToFit() {
        int width = this.height();
        
        if (this.width() != CG.NULL) {
            width = this.getFont().getHeight();
        }

        CGSize size = CG.stringSize(this.text.getString(), this.getFont(), new CGSize(width, Integer.MAX_VALUE));

        CGInsets insets = this.contentInset();

        int widthValue = size.width + insets.horizontal();
        this.widthBinding.sendValue(new Int(widthValue));
        
        int heightValue = size.height + insets.vertical();
        this.heightBinding.sendValue(new Int(heightValue));
        
        return this;
    }

    protected void updateIntrinsicContentSize() {
        S.println(this + " WILL UPDATE INTRINSIC! FONT HEIGHT = " + getFont().getHeight());
        super.updateIntrinsicContentSize();
        String text = this.text.getString();
        
        CGSize size = frame().getCGSize();
        size.width -= contentInset().horizontal();
        size.height -= contentInset().vertical();

        Font font = getFont();

        if (size.width <= 0) {
            size.height = font.getHeight();
            size.width = font.stringWidth(text);
        } else {
            size.height = CG.stringSize(text, font, new CGSize(size.width, this.maxHeight())).height;
        }

        if (!size.equals(this.intrinsicContentSize())) {
            S.println(this + "YEAH IT WILL UPDATE INTRINSIC! HEIGHT = " + size.height);
            intrinsicContentSizeBinding.sendValue(size);
        }
    }
}