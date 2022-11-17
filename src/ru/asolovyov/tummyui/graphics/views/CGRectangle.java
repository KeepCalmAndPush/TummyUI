/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;

/**
 *
 * @author Администратор
 */
public class CGRectangle extends CGSomeDrawable {

    public void draw(Graphics g) {
        super.draw(g);

        CGFrame frame = intrinsicAwareFrame();
        if (frame == null) {
            return;
        }

        CGInsets insets = this.contentInsetBinding.getCGInsets();

        int foregroundColor = this.getColor();
        if (foregroundColor != CG.VALUE_NOT_SET) {
            g.setColor(foregroundColor);
            g.fillRoundRect(
                    frame.x + insets.left,
                    frame.y + insets.top,
                    frame.width - insets.left - insets.right,
                    frame.height - insets.top - insets.bottom,
                    getCornerRadius().width,
                    getCornerRadius().height);
        }

        int strokeColor = this.getBorderColor();
        if (strokeColor != CG.VALUE_NOT_SET) {
            g.setStrokeStyle(this.getStrokeStyle());
            g.setColor(strokeColor);

            g.drawRoundRect(
                    frame.x,
                    frame.y,
                    frame.width,
                    frame.height,
                    getCornerRadius().width,
                    getCornerRadius().height);
        }
    }
}