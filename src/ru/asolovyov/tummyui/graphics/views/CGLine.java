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
    public void draw(Graphics g) {
        super.draw(g);
        CGFrame frame = getCGFrame();
        if (frame == null) {
            return;
        }

        CGInsets insets = this.contentInsetBinding.getCGInsets();

        int color = this.getColor();
        
        if (color != CG.VALUE_NOT_SET) {
            g.setColor(color);
            g.setStrokeStyle(this.getStrokeStyle());
            g.drawLine(
                frame.x + insets.left,
                frame.y + insets.top,
                frame.width - insets.left - insets.right,
                frame.height - insets.top - insets.bottom
                );
        }
    }
}