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
        return new CGFrame();
    }

    public int x = 0,
        y = 0,
        width = 0,
        height = 0;
}
