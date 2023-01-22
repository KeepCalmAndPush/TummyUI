/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;

/**
 *
 * @author Администратор
 */
public class CGLine extends CGSomeDrawable {
    protected void drawContent(Graphics g, CGFrame frame) {
        CGInsets insets = this.contentInset();

        int color = this.color();
        
        if (color == CG.NULL) {
            return;
        }

        g.setColor(color);
        g.setStrokeStyle(this.strokeStyle());
        g.drawLine(
                frame.x + insets.left,
                frame.y + insets.top,
                frame.width - insets.left - insets.right,
                frame.height - insets.top - insets.bottom);
    }
}