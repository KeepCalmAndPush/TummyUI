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
