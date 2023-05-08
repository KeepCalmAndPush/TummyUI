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
public final class CGSize {
    public int width = 0, height = 0;
    
    public static CGSize zero() {
        return new CGSize(0, 0);
    }

    public CGSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public CGSize(CGSize size) {
        this.width = size.width;
        this.height = size.height;
    }

    public CGSize copy() {
        return new CGSize(this);
    }

    public String toString() {
        return (S.stripPackageName(super.toString()) + " " + width + "," + height);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CGSize) {
            CGSize frame = (CGSize)obj;
            return (width == frame.width) && (height == frame.height);
        }
        return false;
    }

    public int hashCode() {
        return ("width"+width+"height"+height).hashCode();
    }
}
