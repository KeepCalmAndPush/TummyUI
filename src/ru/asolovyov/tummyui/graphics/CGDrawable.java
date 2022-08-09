/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;

/**
 *
 * @author Администратор
 */
public interface CGDrawable {
    public void draw(Graphics g);

    public void needsRedraw();
    public void needsRelayout();

    public CGCanvas getCanvas();
    public CGDrawable setCanvas(CGCanvas canvas);

    public CGDrawable sizeToFit();
    public CGSize intrinsicContentSize();

    public CGDrawable color(int colorHex);

    public CGDrawable setFrame(Obj frame);
    public CGDrawable setFrame(CGFrame frame);
    public CGDrawable setFrame(int x, int y, int width, int height);

    public CGDrawable setSize(Obj size);
    public CGDrawable setSize(CGSize size);
    public CGDrawable setSize(int width, int height);

    public CGDrawable width(Int width);
    public CGDrawable width(int width);

    public CGDrawable height(Int height);
    public CGDrawable height(int height);

    public CGFrame getFrame();

    public CGDrawable isVisible(boolean isVisible);

    public CGDrawable color(Int colorHex);
    public CGDrawable isVisible(Bool isVisible);
}

