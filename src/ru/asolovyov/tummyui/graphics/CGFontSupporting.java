/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Font;
import ru.asolovyov.combime.bindings.Obj;

/**
 *
 * @author Администратор
 */
public interface CGFontSupporting extends CGDrawable {
    public CGFontSupporting font(Font font);
    public CGFontSupporting font(Obj font);
}