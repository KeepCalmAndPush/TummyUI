/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.sequence.Drop;
import ru.asolovyov.combime.operators.timing.Debounce;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;

/**
 *
 * @author Администратор
 */

public class CGCanvas extends Canvas {
    private CGDrawable[] content;
    private Int backgroundColor;
    private Bool needsRepaint = new Bool(false);
    public Bool needsRepaint() {
        return needsRepaint;
    }

    private Int keyPressed = (Int) new Int(null).to(new Drop(1)); //TODO оформить в оператор-метод
    private Int keyReleased = (Int) new Int(null).to(new Drop(1));
    private Int keyRepeated = (Int) new Int(null).to(new Drop(1));

    public CGCanvas(CGDrawable content) {
        this(new CGDrawable[] { content });
    }

    public CGCanvas(CGDrawable[] content) {
        super();
        this.content = content;
        if (content.length == 1) {
            CGDrawable child = content[0];
            CGFrame frame = child.frame();
            
            frame.width = Math.min(this.getWidth(), child.maxWidth());
            frame.height = Math.min(this.getHeight(), child.maxHeight());
            child.width(frame.width);
            child.height(frame.height);
        }
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.canvas(this);
        }
        this.backgroundColor(0);
        this.needsRepaint.to(new Debounce(33)).sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
    }

    public void repaint(CGFrame frame) {
        if (frame == null) {
            this.repaint();
            return;
        }
        this.repaint(frame.x, frame.y, frame.width, frame.height);
    }

    protected void paint(Graphics g) {
        g.setColor(this.backgroundColor.getInt());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.draw(g);
        }
    }

    public CGCanvas backgroundColor(int colorHex) {
        return this.backgroundColor(new Int(colorHex));
    }

    public CGCanvas backgroundColor(Int backgroundColorHex) {
        this.backgroundColor = backgroundColorHex;
        this.backgroundColor.sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
        return this;
    }

    protected void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        this.getKeyPressed().setInt(keyCode);
    }

    protected void keyReleased(int keyCode) {
        super.keyReleased(keyCode);
        this.getKeyReleased().setInt(keyCode);
    }

    protected void keyRepeated(int keyCode) {
        super.keyRepeated(keyCode);
        this.getKeyRepeated().setInt(keyCode);
    }
    
    public Int getKeyPressed() {
        return keyPressed;
    }

    public Int getKeyReleased() {
        return keyReleased;
    }

    public Int getKeyRepeated() {
        return keyRepeated;
    }
}
