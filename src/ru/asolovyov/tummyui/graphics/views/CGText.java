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
    private Int alignment = new Int(CG.TOP | CG.LEFT);

    public CGText() {
        super();

        text = new Str("");
        font = new Obj(Font.getDefaultFont());
        alignment = new Int(CG.TOP | CG.LEFT);

        this.updateContentInset();
        this.flexibility(new int[]{ CGDrawable.FLEXIBILITY_DEFAULT, CGDrawable.FLEXIBILITY_NONE });

        this.text.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                S.println("CGTEXT TEXT: " + value);
                relayout();
            }
        });

        this.alignment.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });

        this.font.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                updateContentInset();
                relayout();
            }
        });
    }
    
    public CGText text(Str text) {
        text.route(this.text);
        return this;
    }

    public CGText text(String text) {
        S.println("CGTEXT SET TEXT: " + text);
        this.text.setString(text);
        return this;
    }

    public CGText alignment(int anchor) {
        this.alignment.setInt(anchor);
        return this;
    }
    
    public CGText alignment(Int anchor) {
        anchor.route(this.alignment);
        return this;
    }

    private void updateContentInset() {
        int inset = this.getFont().getHeight() / 4;
        this.contentInset(inset / 2, inset, inset / 2, inset);
    }

    private int alignment() {
        return this.alignment.getInt();
    }

    public String text() {
        if (this.text == null) {
            return "";
        }
        return this.text.getString();
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
        int anchor = this.alignment();

        frame.width -= contentInset.horizontal();
        frame.height -= contentInset.vertical();

        MultilineText multilineText = CG.makeMultilineText(text, font, frame.getCGSize());
        
        int lineHeight = font.getHeight();
        for (int i = 0; i < multilineText.lines.size(); i++) {
            String line = (String) multilineText.lines.elementAt(i);
            int lineWidth = font.stringWidth(line);
            int textY = frame.y + i * lineHeight + contentInset.top;
            int textX = frame.x + contentInset.left;

            if (CG.isBitSet(anchor, CG.VCENTER)) {
                textY += (frame.height - multilineText.height) / 2;
            } else if (CG.isBitSet(anchor, CG.BOTTOM)) {
                textY += (frame.height - multilineText.height);
            }

            if (CG.isBitSet(anchor, CG.HCENTER)) {
                textX += (frame.width - lineWidth) / 2;
            } else if (CG.isBitSet(anchor, CG.RIGHT)) {
                textX += (frame.width - lineWidth);
            }

            S.println("CGTEXT: " + this.frame() + "; ContentInset " + contentInset);
            S.println("CGTEXT WILL DRAW TEXT LINE: " + line + " AT x: " + textX + ", y: " + textY);
            g.drawString(line, textX, textY, 0);
        }
    }

    public CGFontSupporting font(Font font) {
        this.font.setObject(font);
        return this;
    }

    public CGFontSupporting font(Obj font) {
        font.route(this.font);
        return this;
    }

    private Font getFont() {
        return (Font)this.font.getObject();
    }

    public CGDrawable sizeToFit() {
        int width = this.width();

        CGSize size = CG.stringSize(
                this.text.getString(),
                this.getFont(),
                new CGSize(width, Integer.MAX_VALUE)
                );

        CGInsets insets = this.contentInset();

        S.println(this + " " + text() + " WILL SIZE TO FIT!");
        int widthValue = size.width + contentInset().horizontal();
        this.widthBinding.setInt(widthValue);
        
        int heightValue = size.height + contentInset().vertical();
        this.heightBinding.setInt(heightValue);
        
        return this;
    }

    protected void updateIntrinsicContentSize() {
        S.println(this + " WILL UPDATE INTRINSIC! FONT HEIGHT = " + getFont().getHeight());
        super.updateIntrinsicContentSize();
        
        String text = this.text.getString();
        
        CGSize size = frame().getCGSize();
        size.width -= contentInset().horizontal();
        
        Font font = getFont();

        if (size.width <= 0) {
            size.width = font.stringWidth(text);
            size.height = font.getHeight();
        } else {
            size.height = CG.stringSize(
                    text,
                    font,
                    new CGSize(
                        size.width,
                        this.maxHeight()
                    )).height;
        }

        size.width += contentInset().horizontal();
        size.height += contentInset().vertical();
//
//        if (width() > 0) {
//            size.width = Math.min(width(), size.width);
//        }
//
        if (!size.equals(this.intrinsicContentSize())) {
            S.println(this + " YEAH IT WILL UPDATE INTRINSIC! WIDTH: " + size.width + " HEIGHT = " + size.height);
            intrinsicContentSizeBinding.sendValue(size);
        }
    }

    public String toString() {
        return text() + " " + super.toString();
    }
}