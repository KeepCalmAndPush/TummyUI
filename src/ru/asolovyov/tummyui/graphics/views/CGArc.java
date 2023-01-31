/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.publishers.Publisher;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;

/**
 *
 * @author Администратор
 */
public class CGArc extends CGSomeDrawable {
    protected Int startAngleBinding = new Int(0);
    protected Int arcAngleBinding = new Int(270);

    protected Int fillColor = new Int(CG.NULL);
    protected Int strokeWidth = new Int(1);

    public CGArc() {
        super();
        
        Publisher.combineLatest(new Publisher[] {
            this.startAngleBinding,
            this.arcAngleBinding,
            this.fillColor,
            this.strokeWidth
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
    }

    public int startAngle() {
        return this.startAngleBinding.getInt();
    }

    public CGArc startAngle(Int startAngleBinding) {
        startAngleBinding.route(this.startAngleBinding);
        return this;
    }

    public CGArc arcAngle(Int arcAngleBinding) {
        arcAngleBinding.route(this.arcAngleBinding);
        return this;
    }

    public CGArc startAngle(int startAngleBinding) {
        this.startAngleBinding.setInt(startAngleBinding);
        return this;
    }

    public CGArc arcAngle(int arcAngleBinding) {
        this.arcAngleBinding.setInt(arcAngleBinding);
        return this;
    }

    public int fillColor() {
        return fillColor.getInt();
    }

    public CGArc fillColor(int colorHex) {
        this.fillColor.setInt(colorHex);
        return this;
    }

    public CGArc fillColor(Int fillColor) {
        fillColor.route(this.fillColor);
        return this;
    }
    
    public int strokeWidth() {
        return strokeWidth.getInt();
    }

    public CGArc strokeWidth(int width) {
        this.strokeWidth.setInt(width);
        return this;
    }

    public CGArc strokeWidth(Int width) {
        width.route(this.strokeWidth);
        return this;
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        CGInsets insets = this.contentInset();

        int fillColor = this.fillColor();
        if (fillColor != CG.NULL) {
            g.setColor(fillColor);
            g.fillArc(
                frame.x + insets.left,
                frame.y + insets.top,
                frame.width - insets.left - insets.right,
                frame.height - insets.top - insets.bottom,
                startAngleBinding.getInt(),
                arcAngleBinding.getInt()
                );
        }

        int color = this.color();
        if (color != CG.NULL) {
            g.setColor(color);
            g.setStrokeStyle(this.strokeStyle());

            int innerColor = fillColor != CG.NULL ? fillColor : backgroundColor();
            if (innerColor == CG.NULL || strokeWidth() <= 1) {
                for(int i = 0; i < strokeWidth(); i++) {
                    g.drawArc(
                        frame.x + insets.left + i,
                        frame.y + insets.top + i,
                        frame.width - insets.left - insets.right - 2*i,
                        frame.height - insets.top - insets.bottom - 2*i,
                        startAngleBinding.getInt(),
                        arcAngleBinding.getInt());
                }
                return;
            }

            g.fillArc(
                        frame.x + insets.left,
                        frame.y + insets.top,
                        frame.width - insets.left - insets.right,
                        frame.height - insets.top - insets.bottom,
                        startAngleBinding.getInt(),
                        arcAngleBinding.getInt());

            int width1 = strokeWidth();
            int width2 = 2*width1;

           g.setColor(innerColor);
           g.fillArc(
                        frame.x + insets.left + width1,
                        frame.y + insets.top + width1,
                        frame.width - insets.left - insets.right - width2,
                        frame.height - insets.top - insets.bottom - width2,
                        startAngleBinding.getInt() - width2,
                        359);
        }
    }
}
