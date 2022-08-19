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

/**
 *
 * @author Администратор
 */
public class CGText extends CGSomeDrawable implements CGFontSupporting {
    private Str text;
    private Obj font;
    private Int anchor;

    CGText(Str text) {
        super();
        this.text = text;
        this.text.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
    }

    public CGText text(Str text) {
        this.text = text;
        this.text.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }

    public CGText text(String text) {
        return this.text(new Str(text));
    }

    public CGText anchor(int anchor) {
        return this.anchor(new Int(anchor));
    }

    public CGText anchor(Int anchor) {
        this.anchor = anchor;
        this.anchor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    private int getAnchor() {
        if (this.anchor != null) {
            return this.anchor.getInt();
        }

        return Graphics.HCENTER;
    }

    public Str text() {
        return this.text;
    }

    public void draw(Graphics g) {
        super.draw(g);
        g.setFont(getFont());
        CGFrame frame = getCGFrame();
        g.drawString(text.getString(), frame.x, frame.y, this.getAnchor());
    }

    public CGFontSupporting font(Font font) {
        return this.font(new Obj(font));
    }

    public CGFontSupporting font(Obj font) {
        this.font = font;
        this.font.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    private Font getFont() {
        if (this.font != null) {
            return (Font)this.font.getObject();
        }
        return null;
    }
}