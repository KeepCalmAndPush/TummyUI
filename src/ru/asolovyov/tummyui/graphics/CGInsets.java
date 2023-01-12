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

    public CGInsets(CGInsets insets) {
        super();
        this.top = insets.top;
        this.left = insets.left;
        this.bottom = insets.bottom;
        this.right = insets.right;
    }

    public CGInsets copy() {
        return new CGInsets(this);
    }

    public int top = 0,
        left = 0,
        bottom = 0,
        right = 0;

    public int horizontal() {
        return left + right;
    }

    public int vertical() {
        return top + bottom;
    }

    public int deltaX() {
        return +left - right;
    }

    public int deltaY() {
        return +top - bottom;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CGInsets) {
            CGInsets frame = (CGInsets)obj;
            return (top == frame.top) && (left == frame.left) && (bottom == frame.bottom) && (right == frame.right);
        }
        return false;
    }

    public int hashCode() {
        return (top + "" + left + "" + bottom + "" + right).hashCode();
    }
}
