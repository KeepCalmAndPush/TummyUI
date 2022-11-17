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

    public CGCanvas getCanvas();
    public CGDrawable setCanvas(CGCanvas canvas);

    public CGDrawable sizeToFit();
    
    public CGDrawable color(int backgroundColorHex);
    public CGDrawable color(Int backgroundColorHex);

    public CGDrawable backgroundColor(int backgroundColorHex);
    public CGDrawable backgroundColor(Int backgroundColorHex);
    
    public CGDrawable borderColor(int borderColorHex);
    public CGDrawable borderColor(Int borderColorHex);

    public CGDrawable setFrame(Frame frame);
    public CGDrawable setFrame(int x, int y, int width, int height);

    public CGDrawable setOrigin(Point offset);
    public CGDrawable setOrigin(int x, int y);

    public CGDrawable setContentOffset(Point offset);
    public CGDrawable setContentOffset(int x, int y);

    public CGDrawable setContentInset(Insets inset);
    public CGDrawable setContentInset(int top, int left, int bottom, int right);

    public CGDrawable cornerRaduis(CGSize cornerRadius);
    public CGDrawable cornerRaduis(Size cornerRadiusBinding);

    public CGDrawable width(Int width);
    public CGDrawable width(int width);

    public CGDrawable height(Int height);
    public CGDrawable height(int height);
    
    public CGDrawable resizingMask(Int mask);
    public CGDrawable resizingMask(int mask);

    public CGDrawable isVisible(boolean isVisible);
    public CGDrawable isVisible(Bool isVisible);

    public Bool isVisible();
    
    //TODO возможно нам не нужен фрейм. Он будет не для самосоятотельного задания пользоватем.
    //фрейм заполняем в стеках и прочих контейнерах на основании вычислений/интринсиков.
    //тогда если у нас не заданы видс/хайт, то пляшем от интринскика/вычислений
    
    public Point getOrigin();

    public int getWidth();
    public int getHeight();

    //TODO Вертать не публишер, а примитив
    public Point getContentOffset();
    public Insets getContentInset();

    public Int resizingMask();

    public Size intrinsicContentSize();
    public CGSize getCornerRadius();
    
    public CGFrame getCGFrame();
    public CGFrame intrinsicAwareFrame();
}

