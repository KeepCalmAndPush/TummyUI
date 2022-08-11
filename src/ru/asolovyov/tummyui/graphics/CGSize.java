/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

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
}
