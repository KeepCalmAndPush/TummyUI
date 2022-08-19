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
    public static final int FLEXIBLE_LEFT = 1 << 2;
    public static final int FLEXIBLE_TOP = 1 << 3;
    
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

    public int x = 0,
        y = 0,
        width = 0,
        height = 0;

    public CGSize getSize() {
        return new CGSize(width, height);
    }
}
