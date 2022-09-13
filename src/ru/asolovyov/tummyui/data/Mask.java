/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.data;

/**
 *
 * @author Администратор
 */
public class Mask {
    public static boolean isSet(int mask, int bit) {
        return (mask & bit) == bit;
    }
}
