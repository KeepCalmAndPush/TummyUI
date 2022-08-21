/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.tummyui.bindings.Frame;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.bindings.Size;

/**
 *
 * @author Администратор
 */
public interface CGDrawable {
    public static abstract class GeometryReader {
        public abstract void read(CGDrawable self, CGFrame frame);
    }

    public static class KeyboardHandler {
        public void keyPressed(CGDrawable self, int keyCode) { }
        public void keyReleased(CGDrawable self, int keyCode) { }
        public void keyRepeated(CGDrawable self, int keyCode) { }
    }

    public void draw(Graphics g);

    public void needsRedraw();
    public void needsRelayout(CGFrame frame);

    public CGDrawable readGeometry(GeometryReader reader);
    public GeometryReader getGeometryReader();

    public CGDrawable handleKeyboard(KeyboardHandler handler);
    public KeyboardHandler getKeyboardHandler();

    public CGCanvas getCanvas();
    public CGDrawable setCanvas(CGCanvas canvas);

    public CGDrawable sizeToFit();

    public CGDrawable color(int colorHex);
    public CGDrawable color(Int colorHex);

    public CGDrawable setFrame(Frame frame);
    public CGDrawable setFrame(int x, int y, int width, int height);

    public CGDrawable setOffset(Point offset);
    public CGDrawable setOffset(int x, int y);

    public CGDrawable width(Int width);
    public CGDrawable width(int width);

    public CGDrawable height(Int height);
    public CGDrawable height(int height);
    
    public CGDrawable resizingMask(Int mask);
    public CGDrawable resizingMask(int mask);

    public CGDrawable isVisible(boolean isVisible);
    public CGDrawable isVisible(Bool isVisible);

    public Bool isVisible();
    public Frame getFrame();
    public Point getOffset();
    public Int resizingMask();
    public Size intrinsicContentSize();

    public CGFrame getCGFrame();
}

