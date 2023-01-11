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

    public CGDrawable stroke(int strokeStyle);
    public CGDrawable stroke(Int strokeStyle);

    public void needsRedraw();
    public void needsRelayout(CGFrame frame);

    public CGDrawable readGeometry(GeometryReader reader);
    public GeometryReader geometryReader();

    public CGDrawable handleKeyboard(KeyboardHandler handler);
    public KeyboardHandler keyboardHandler();

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
    public CGDrawable origin(Point origin);
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

    public int x();
    public CGDrawable x(Int x);
    public CGDrawable x(int x);

    public int y();
    public CGDrawable y(Int y);
    public CGDrawable y(int y);

    public int minX();
    public CGDrawable minX(Int minX);
    public CGDrawable minX(int minX);

    public int maxX();
    public CGDrawable maxX(Int maxX);
    public CGDrawable maxX(int maxX);
    
    public int minY();
    public CGDrawable minY(Int minY);
    public CGDrawable minY(int minY);

    public int maxY();
    public CGDrawable maxY(Int maxY);
    public CGDrawable maxY(int maxY);

    public int width();
    public CGDrawable width(Int width);
    public CGDrawable width(int width);

    public int minWidth();
    public CGDrawable minWidth(Int minWidth);
    public CGDrawable minWidth(int minWidth);

    public int maxWidth();
    public CGDrawable maxWidth(Int maxWidth);
    public CGDrawable maxWidth(int maxWidth);

    public int height();
    public CGDrawable height(Int height);
    public CGDrawable height(int height);

    public int minHeight();
    public CGDrawable minHeight(Int minHeight);
    public CGDrawable minHeight(int minHeight);

    public int maxHeight();
    public CGDrawable maxHeight(Int maxHeight);
    public CGDrawable maxHeight(int maxHeight);

    public boolean hasShrinkableWidth();
    public boolean hasGrowableWidth();
    public boolean hasShrinkableHeight();
    public boolean hasGrowableHeight();
    
    public boolean isVisible();
    public CGDrawable isVisible(boolean isVisible);
    public CGDrawable isVisible(Bool isVisible);
}

