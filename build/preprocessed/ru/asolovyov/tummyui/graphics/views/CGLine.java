/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.filtering.RemoveDuplicates;
import ru.asolovyov.combime.publishers.Publisher;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;

/**
 *
 * @author Администратор
 */
public class CGLine extends CGSomeDrawable {
    protected Int strokeWidth = new Int(1);
    protected Bool isInverted = new Bool(false);

    public CGLine() {
        super();

        Publisher.combineLatest(new Publisher[] {
            this.isInverted,
            this.strokeWidth
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
    }
    
    public int strokeWidth() {
        return strokeWidth.getInt();
    }

    public CGLine strokeWidth(int width) {
        this.strokeWidth.setInt(width);
        return this;
    }

    public CGLine strokeWidth(Bool width) {
        width.route(this.strokeWidth);
        return this;
    }

    public boolean isInverted() {
        return this.isInverted.getBoolean();
    }

    public CGLine isInverted(boolean alignment) {
        this.isInverted.setBool(alignment);
        return this;
    }

    public CGLine isInverted(Int alignment) {
        alignment.route(this.isInverted);
        return this;
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        CGInsets insets = this.contentInset();

        int color = this.color();

        if (color != CG.NULL) {
            g.setColor(color);
            g.setStrokeStyle(this.strokeStyle());

            int strokeWidth = strokeWidth();
            int start = -strokeWidth / 2;
            int end = strokeWidth + start;

            for(int i = start; i < end; i++) {
                int x1 = frame.x + insets.left + i;
                int x2 = frame.maxX() - insets.horizontal() + i;

                int y1 = !isInverted() 
                        ? frame.y + insets.top
                        : frame.maxY() - insets.vertical();

                int y2 = !isInverted()
                        ? frame.maxY() - insets.vertical()
                        : frame.y + insets.top;

                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}