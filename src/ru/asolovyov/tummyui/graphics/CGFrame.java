/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

/**
 *
 * @author Администратор
 */
public final class CGFrame {
    public static final int FLEXIBLE_WIDTH = 1;
    public static final int FLEXIBLE_HEIGHT = 1 << 1;
    public static final int FLEXIBLE_X = 1 << 2;
    public static final int FLEXIBLE_Y = 1 << 3;

    public static final int FLEXIBLE_ORIGIN = FLEXIBLE_X | FLEXIBLE_Y;
    public static final int FLEXIBLE_SIZE = FLEXIBLE_WIDTH | FLEXIBLE_HEIGHT;
    public static final int FLEXIBLE_ALL = (1 << 4) - 1;

    public static CGFrame zero() {
        return new CGFrame(0, 0, 0, 0);
    }

    public CGFrame(int x, int y, int width, int height) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public CGFrame(CGFrame frame) {
        super();
        this.x = frame.x;
        this.y = frame.y;
        this.width = frame.width;
        this.height = frame.height;
    }

    public CGFrame copy() {
        return new CGFrame(this);
    }

    public int x = 0,
        y = 0,
        width = 0,
        height = 0;

    public CGSize getCGSize() {
        return new CGSize(width, height);
    }

    public int maxY() {
        return this.y + this.height;
    }

    public int maxX() {
        return this.x + this.width;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CGFrame) {
            CGFrame frame = (CGFrame)obj;
            return (x == frame.x) && (y == frame.y) && (width == frame.width) && (height == frame.height);
        }
        return false;
    }

    public int hashCode() {
        return ("x"+x+"y"+y+"width"+width+"height"+height).hashCode();
    }

    
}
