/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.common.S;

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

    public CGFrame(CGFrame frame) {
        super();
        this.x = frame.x;
        this.y = frame.y;
        this.width = frame.width;
        this.height = frame.height;
    }

    public CGFrame origin(CGPoint origin) {
        CGFrame copy = this.copy();
        copy.x = origin.x;
        copy.y = origin.y;

        return this;
    }

    public CGFrame size(CGSize size) {
        CGFrame copy = this.copy();
        copy.width = size.width;
        copy.height = size.height;

        return this;
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
        return ("" + x + y + width + height).hashCode();
    }

    public String toString() {
        return S.stripPackageName(super.toString()) + " (" + x + "," + y + "; " + width + "," + height + ")";
    }
}
