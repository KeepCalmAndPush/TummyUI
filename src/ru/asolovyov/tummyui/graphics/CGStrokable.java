/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.bindings.IntBinding;

/**
 *
 * @author Администратор
 */
public interface CGStrokable extends CGDrawable {
    public CGStrokable stroke(int strokeStyle);
    public CGStrokable stroke(IntBinding strokeStyle);
}
