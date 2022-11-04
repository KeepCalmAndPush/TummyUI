/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.graphics.CG.MultilineText;

/**
 *
 * @author Администратор
 */
public class CGText extends CGSomeDrawable implements CGFontSupporting {
    private Str text = new Str("");
    private Obj font = new Obj(Font.getDefaultFont());
    private Int anchor = new Int(CG.CENTER);
    private Int textColor = new Int(0x000000);

    public CGText(Str text) {
        super();
        this.text = text;
        this.text.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });

        this.textColor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
    }
    
    public CGText text(Str text) {
        text.route(this.text);
        return this;
    }

    public CGText text(String text) {
        this.text.setString(text);
        return this;
    }

    public CGText alignment(int anchor) {
        return this.anchor(new Int(anchor));
    }
    
    public CGText anchor(Int anchor) {
        anchor.route(this.anchor);
        return this;
    }

    private int getAnchor() {
        return this.anchor.getInt();
    }

    public Str text() {
        return this.text;
    }

    public void draw(Graphics g) {
        super.draw(g);
        CGFrame frame = getCGFrame();
        if (frame == null) {
            return;
        }
        g.setFont(getFont());
        g.setColor(this.textColor.getInt());

        String text = this.text.getString();
        Font font = this.getFont();
        int anchor = this.getAnchor();

        MultilineText multilineText = CG.makeMultilineText(text, font, frame.getCGSize());
        
        int lineHeight = font.getHeight();
        for ( int i = 0; i < multilineText.lines.size(); i++) {
            String line = (String) multilineText.lines.elementAt(i);
            int lineWidth = font.stringWidth(line);
            int textY = frame.y + i * lineHeight;
            int textX = frame.x;

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
            
            g.drawString(line, textX, textY, 0);
        }
    }

    public CGFontSupporting font(Font font) {
        return this.font(new Obj(font));
    }

    public CGFontSupporting font(Obj font) {
        font.route(this.font);
        return this;
    }

    private Font getFont() {
        return (Font)this.font.getObject();
    }

    public CGFontSupporting textColor(int textColorHex) {
        this.textColor.setInt(textColorHex);
        return this;
    }

    public CGFontSupporting textColor(Int textColorHex) {
        this.textColor = textColorHex;
        this.textColor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }
}