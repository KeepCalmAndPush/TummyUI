/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;

/**
 *
 * @author Администратор
 */
public interface CGDrawable {
    public void draw(Graphics g);

    public void needsRedraw();
    public void needsRelayout();

    public CGDrawable canvas(CGCanvas canvas);

    public CGDrawable color(int colorHex);
    public CGDrawable frame(CGFrame frame);
    public CGDrawable frame(int x, int y, int width, int height);
    public CGDrawable isVisible(boolean isVisible);

    public CGDrawable color(IntBinding colorHex);
    public CGDrawable frame(ObjectBinding frame);
    public CGDrawable isVisible(BoolBinding isVisible);
}

