/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {
    protected Int color;
    protected Obj frameBinding;
    protected Bool isVisible;

    private CGCanvas canvas;

    public void draw(Graphics g) {
        g.setColor(this.getColor());
    }

    public CGDrawable color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGDrawable setFrame(int x, int y, int width, int height) {
        return this.setFrame(new CGFrame(x, y, width, height));
    }

    public CGDrawable color(Int colorHex) {
        this.color = colorHex;
        this.color.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable setFrame(CGFrame frame) {
        return this.setFrame(new Obj(frame));
    }

    public CGDrawable setFrame(Obj frame) {
        this.frameBinding = frame;
        return this;
    }

    public void needsRedraw() {
        if (this.getCanvas() != null) {
            this.getCanvas().repaint(getFrame());
        }
    }

    public void needsRelayout() {
        this.needsRedraw();
    }

    public CGFrame getFrame() {
        if (frameBinding != null) {
           return (CGFrame) frameBinding.getObject();
        }
        return CGFrame.zero();
    }

    protected int getColor() {
        if (color != null) {
           return color.getInt();
        }
        return 0x00000000;
    }

    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(new Bool(isVisible));
    }

    public CGDrawable isVisible(Bool isVisible) {
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

    public CGDrawable setCanvas(CGCanvas canvas) {
        this.canvas = canvas;
        canvas.needsRepaint().setBool(true);

        return this;
    }
    
    public CGCanvas getCanvas() {
        return canvas;
    }

    public CGDrawable sizeToFit() {
        return this;
    }

    public CGSize intrinsicContentSize() {
        return getFrame().getSize();
    }
}