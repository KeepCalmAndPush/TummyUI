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
    private Int startAngleBinding;
    private Int endAngleBinding;

    public CGArc() {
        super();
        
        Publisher.combineLatest(new Publisher[] {
            this.startAngleBinding,
            this.endAngleBinding
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                relayout();
            }
        });
    }

    public CGArc startAngle(Int startAngleBinding) {
        startAngleBinding.route(this.startAngleBinding);
        return this;
    }

    public CGArc endAngle(Int endAngleBinding) {
        endAngleBinding.route(this.endAngleBinding);
        return this;
    }

    public CGArc startAngle(int startAngleBinding) {
        this.startAngleBinding.setInt(startAngleBinding);
        return this;
    }

    public CGArc endAngle(int endAngleBinding) {
        this.endAngleBinding.setInt(endAngleBinding);
        return this;
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        CGInsets insets = this.contentInset();

        int fillColor = this.color();
        if (fillColor != CG.NULL) {
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

        int color = this.color();
        if (color != CG.NULL) {
            g.setColor(color);
            g.setStrokeStyle(this.strokeStyle());
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
