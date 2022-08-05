/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.B;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.threading.Clock;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;

/**
 *
 * @author Администратор
 */
public class Canvas extends UIMIDlet {
    Clock clock;
    int i = 0;
    IntBinding arcColor = B.Int(0xFFFF00);
    IntBinding backColor = B.Int(0x0000FF);

    {
        clock = new Clock(500);
        clock.add(new Runnable() {
            public void run() {
                if (i++%2 == 0) {
                    int arc = arcColor.getInt();
                    arcColor.setInt(backColor.getInt());
                    backColor.setInt(arc);
                }
            }
        });
    }
    
    protected Displayable content() {
        // сюда отлично вольется геометри ридер
        return CG.Canvas(
                CG.Arc(30, 300)
                        .color(arcColor)
                        .frame(10, 10, 100, 100),
                CG.Circle()
                        .color(0xFF0000)
                        .frame(75, 25, 10, 10)

                ).color(backColor);
    }
}
