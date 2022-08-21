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
    private Str text = new Str("");
    private Obj font = new Obj(Font.getDefaultFont());
    private Int anchor = new Int(Graphics.LEFT | Graphics.TOP);

    public CGText(Str text) {
        super();
        this.text = text;
        this.text.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
    }

    public CGText text(Str text) {
        text.sink(new Sink() {
            protected void onValue(Object value) {
                CGText.this.text.sendValue(value);
            }
        });
        return this;
    }

    public CGText text(String text) {
        this.text.setString(text);
        return this;
    }

    public CGText anchor(int anchor) {
        return this.anchor(new Int(anchor));
    }

    /**
     * anchor point must be one of the horizontal constants
     * (LEFT, HCENTER, RIGHT) combined with one of the vertical constants
     * (TOP, BASELINE, BOTTOM)
     */
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
        return this.anchor.getInt();
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
        return (Font)this.font.getObject();
    }
}