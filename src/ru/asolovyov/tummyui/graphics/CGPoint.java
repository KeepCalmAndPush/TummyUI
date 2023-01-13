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
public final class CGPoint {
    public int x = 0, y = 0;
    
    public static CGPoint zero() {
        return new CGPoint(0, 0);
    }

    public CGPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CGPoint(CGPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public CGPoint copy() {
        return new CGPoint(this);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CGPoint) {
            CGPoint frame = (CGPoint)obj;
            return (x == frame.x) && (y == frame.y);
        }
        return false;
    }

    public int hashCode() {
        return ("" + x + y).hashCode();
    }

    public String toString() {
        return (S.stripPackageName(super.toString()) + " " + x + "," + y);
    }
}
