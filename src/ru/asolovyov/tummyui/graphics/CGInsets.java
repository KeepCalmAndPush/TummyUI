/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

/**
 *
 * @author Администратор
 */
public class CGInsets {
    public static CGInsets zero() {
        return new CGInsets(0, 0, 0, 0);
    }
    
    public CGInsets(int top, int left, int bottom, int right) {
        super();
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public int top = 0,
        left = 0,
        bottom = 0,
        right = 0;
}
