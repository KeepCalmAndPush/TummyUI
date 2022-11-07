/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import ru.asolovyov.tummyui.graphics.views.CGDrawable;
import javax.microedition.lcdui.Font;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;

/**
 *
 * @author Администратор
 */
public interface CGFontSupporting extends CGDrawable {
    public CGFontSupporting font(Font font);
    public CGFontSupporting font(Obj font);

    public CGFontSupporting textColor(int backgroundColorHex);
    public CGFontSupporting textColor(Int backgroundColorHex);
}