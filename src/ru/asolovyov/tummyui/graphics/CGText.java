/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGText extends CGSomeDrawable implements CGFontSupporting {
    private StringBinding text;
    private ObjectBinding font;
    private IntBinding anchor;

    CGText(StringBinding text) {
        super();
        this.text = text;
        this.text.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
            }
        });
    }

    public CGText anchor(int anchor) {
        return this.anchor(Binding.Int(anchor));
    }

    public CGText anchor(IntBinding anchor) {
        this.anchor = anchor;
        this.anchor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
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

    public StringBinding text() {
        return this.text;
    }

    public void draw(Graphics g) {
        super.draw(g);
        g.setFont(getFont());
        CGFrame frame = getFrame();
        g.drawString(text.getString(), frame.x, frame.y, this.getAnchor());
    }

    public CGFontSupporting font(Font font) {
        return this.font(Binding.Object(font));
    }

    public CGFontSupporting font(ObjectBinding font) {
        this.font = font;
        this.font.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
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