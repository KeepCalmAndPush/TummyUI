/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.B;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {
    protected IntBinding color;
    protected ObjectBinding frameBinding;
    protected BoolBinding isVisible;

    protected CGCanvas canvas;

    public void draw(Graphics g) {
        g.setColor(this.getColor());
    }

    public CGDrawable color(int colorHex) {
        return this.color(B.Int(colorHex));
    }

    public CGDrawable frame(int x, int y, int width, int height) {
        return this.frame(new CGFrame(x, y, width, height));
    }

    public CGDrawable color(IntBinding colorHex) {
        this.color = colorHex;
        this.color.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable frame(CGFrame frame) {
        return this.frame(B.Object(frame));
    }

    public CGDrawable frame(ObjectBinding frame) {
        this.frameBinding = frame;
        return this;
    }

    public void needsRedraw() {
        if (this.canvas != null) {
            this.canvas.repaint(getFrame());
        }
    }

    public void needsRelayout() {
        this.needsRedraw();
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.canvas = canvas;
        this.needsRedraw();
        
        return this;
    }

    protected CGFrame getFrame() {
        if (frameBinding != null) {
           return (CGFrame) frameBinding.getObject();
        }
        return null;
    }

    protected int getColor() {
        if (color != null) {
           return color.getInt();
        }
        return 0x00000000;
    }

    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(B.Bool(isVisible));
    }

    public CGDrawable isVisible(BoolBinding isVisible) {
        this.isVisible = isVisible;
        this.isVisible.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
            }
        });
        return this;
    }

    public boolean isVisible() {
        if (this.isVisible != null) {
            return this.isVisible.getBool();
        }
        return true;
    }
}