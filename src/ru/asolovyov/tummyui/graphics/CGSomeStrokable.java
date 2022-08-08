/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeStrokable extends CGSomeDrawable {
    protected Int strokeStyle;

    public CGSomeStrokable stroke(int strokeStyle) {
        return this.stroke(new Int(strokeStyle));
    }

    public CGSomeStrokable stroke(Int strokeStyle) {
        this.strokeStyle = strokeStyle;
        this.strokeStyle.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public void draw(Graphics g) {
        super.draw(g);
        g.setStrokeStyle(getStrokeStyle());
    }

    protected int getStrokeStyle() {
        if (strokeStyle != null) {
           return strokeStyle.getInt();
        }
        return Graphics.SOLID;
    }
}
