/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;

/**
 *
 * @author Администратор
 */
public class CGLine extends CGSomeDrawable {
    public void draw(Graphics g) {
        super.draw(g);
        CGFrame frame = this.getCGFrame();
        g.drawLine(
                frame.x,
                frame.y,
                frame.x + frame.width,
                frame.y + frame.height);
    }
}