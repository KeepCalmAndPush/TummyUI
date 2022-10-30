/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

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
}
