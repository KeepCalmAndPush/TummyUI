/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;

/**
 *
 * @author Администратор
 */
public class CGArc extends CGSomeDrawable {
    private Int startAngleBinding;
    private Int endAngleBinding;

    public CGArc startAngle(Int startAngleBinding) {
        this.startAngleBinding = startAngleBinding;
        this.startAngleBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGArc endAngle(Int endAngleBinding) {
        this.endAngleBinding = endAngleBinding;
        this.endAngleBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public void draw(Graphics g) {
        super.draw(g);

        CGFrame frame = getCGFrame();
        if (frame == null) {
            return;
        }

        CGInsets insets = this.contentInsetBinding.getCGInsets();

        int fillColor = this.getColor();
        if (fillColor != CG.VALUE_NOT_SET) {
            g.setColor(fillColor);
            g.fillArc(
                frame.x + insets.left,
                frame.y + insets.top,
                frame.width - insets.left - insets.right,
                frame.height - insets.top - insets.bottom,
                startAngleBinding.getInt(),
                endAngleBinding.getInt()
                );
        }

        int color = this.getColor();
        if (color != CG.VALUE_NOT_SET) {
            g.setColor(color);
            g.setStrokeStyle(this.getStrokeStyle());
            g.drawArc(
                frame.x + insets.left,
                frame.y + insets.top,
                frame.width - insets.left - insets.right,
                frame.height - insets.top - insets.bottom,
                startAngleBinding.getInt(),
                endAngleBinding.getInt()
                );
        }
    }
}
