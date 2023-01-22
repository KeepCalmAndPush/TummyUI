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

    protected void drawContent(Graphics g, CGFrame frame) {
        CGInsets insets = this.contentInset();
        int cornerRadius = cornerRadius() * 2;
        int borderWidth = this.borderWidth();

        int foregroundColor = this.color();
        if (foregroundColor != CG.NULL) {
            g.setColor(foregroundColor);
            g.fillRoundRect(
                    frame.x + insets.left + borderWidth,
                    frame.y + insets.top + borderWidth,
                    frame.width - insets.horizontal() - 2*borderWidth,
                    frame.height - insets.vertical() - 2*borderWidth,
                    cornerRadius - borderWidth,
                    cornerRadius - borderWidth);
        }
    }
}