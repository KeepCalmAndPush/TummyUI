/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.data.test;

import javax.microedition.lcdui.Displayable;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGCanvas;
import ru.asolovyov.tummyui.graphics.CGDrawable;
import ru.asolovyov.tummyui.graphics.CGFrame;

/**
 *
 * @author Администратор
 */
public class Canvas extends UIMIDlet {
    protected Displayable content() {
        // сюда отлично вольется геометри ридер
        // надо еще запилить тикалку-скедулер, чтобы анимированно менять значения в биндингах
        return CG.Canvas(
                CG.Arc(30, 300)
                        .color(0xFFFF00)
                        .frame(10, 10, 100, 100),
                CG.Circle()
                        .color(0xFF0000)
                        .frame(75, 25, 10, 10)

                ).color(0x0000FF);
    }
}
