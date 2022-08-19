/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.timing.Debounce;

/**
 *
 * @author Администратор
 */

public class CGCanvas extends Canvas {
    private CGDrawable[] content;
    private Int color;
    private Bool needsRepaint = new Bool(false);
    protected Bool needsRepaint() {
        return needsRepaint;
    }

    private Int keyPressed = new Int(-1);
    private Int keyReleased = new Int(-1);
    private Int keyRepeated = new Int(-1);

    public CGCanvas(CGDrawable content) {
        this(new CGDrawable[] { content });
    }

    public CGCanvas(CGDrawable[] content) {
        super();
        this.content = content;
        if (content.length == 1) {
            CGFrame frame = content[0].getCGFrame();
            if (frame.width == CGFrame.FLEXIBLE_WIDTH) {
                frame.width = this.getWidth();
            }
            if (frame.height == CGFrame.FLEXIBLE_HEIGHT) {
                frame.height = this.getHeight();
            }
            content[0].needsRelayout(frame);
        }
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.setCanvas(this);
        }
        this.color(0);
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
        g.setColor(this.color.getInt());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.draw(g);
        }
    }

    public CGCanvas color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGCanvas color(Int colorHex) {
        this.color = colorHex;
        this.color.sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
        return this;
    }
}



