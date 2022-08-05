/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.B;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */

public class CGCanvas extends Canvas {
    private CGDrawable[] content;
    private IntBinding color;

    public CGCanvas(CGDrawable content) {
        this(new CGDrawable[] { content });
    }

    public CGCanvas(CGDrawable[] content) {
        super();
        this.content = content;
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.canvas(this);
        }
        this.color(0);
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
        return this.color(B.Int(colorHex));
    }

    public CGCanvas color(IntBinding colorHex) {
        this.color = colorHex;
        this.color.sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
        return this;
    }
}



