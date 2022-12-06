/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.tummyui.bindings.Frame;
import ru.asolovyov.tummyui.bindings.Insets;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;
import ru.asolovyov.tummyui.graphics.CGPoint;
import ru.asolovyov.tummyui.graphics.CGSize;

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

    public void animate(int durationMillis, Runnable animations);

    public CGDrawable stroke(int strokeStyle);
    public CGDrawable stroke(Int strokeStyle);

    public void needsRedraw();
    public void needsRelayout(CGFrame frame);

    public CGDrawable readGeometry(GeometryReader reader);
    public GeometryReader getGeometryReader();

    public CGDrawable handleKeyboard(KeyboardHandler handler);
    public KeyboardHandler getKeyboardHandler();

    public CGCanvas canvas();
    public CGDrawable canvas(CGCanvas canvas);

    public CGDrawable sizeToFit();
    
    public CGDrawable color(int backgroundColorHex);
    public CGDrawable color(Int backgroundColorHex);

    public CGDrawable backgroundColor(int backgroundColorHex);
    public CGDrawable backgroundColor(Int backgroundColorHex);
    
    public CGDrawable borderColor(int borderColorHex);
    public CGDrawable borderColor(Int borderColorHex);

    public CGSize intrinsicContentSize();
    public CGFrame intrinsicAwareFrame();

    public CGFrame frame();
    public CGDrawable frame(Frame frame);
    public CGDrawable frame(int x, int y, int width, int height);

    public CGPoint origin();
    public CGDrawable origin(Point offset);
    public CGDrawable origin(int x, int y);

    public CGPoint contentOffset();
    public CGDrawable contentOffset(Point offset);
    public CGDrawable contentOffset(int x, int y);

    public CGInsets contentInset();
    public CGDrawable contentInset(Insets inset);
    public CGDrawable contentInset(int top, int left, int bottom, int right);

    public CGSize cornerRadius();
    public CGDrawable cornerRaduis(CGSize cornerRadius);
    public CGDrawable cornerRaduis(Size cornerRadiusBinding);

    public int width();
    public CGDrawable width(Int width);
    public CGDrawable width(int width);

    public int height();
    public CGDrawable height(Int height);
    public CGDrawable height(int height);

    public int flexibility();
    public CGDrawable flexibility(Int mask);
    public CGDrawable flexibility(int mask);

    public boolean isVisible();
    public CGDrawable isVisible(boolean isVisible);
    public CGDrawable isVisible(Bool isVisible);
}

