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
    // TODO сделать ресайзинг маск, а не однократно менять размеры тут
    public final static int AUTOMATIC_DIMENSION = -1;

    public static CGFrame automatic() {
        return new CGFrame(0, 0, AUTOMATIC_DIMENSION, AUTOMATIC_DIMENSION);
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
