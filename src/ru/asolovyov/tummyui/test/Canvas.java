/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.threading.Clock;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGArc;
import ru.asolovyov.tummyui.graphics.CGDrawable;
import ru.asolovyov.tummyui.graphics.CGStack;

/**
 *
 * @author Администратор
 */
public class Canvas extends UIMIDlet {
    Clock clock;
    int i = 0;
    Int arcColor = new Int(0xFFFF00);
    Int backColor = new Int(0x0000FF);
    Bool cond = new Bool(false);

    {
        clock = new Clock(500);
        clock.add(new Runnable() {
            public void run() {
                int j = i++%2;
                cond.setBool(j == 0);
                
                if (j == 0) {
                    int arc = arcColor.getInt();
                    arcColor.setInt(backColor.getInt());
                    backColor.setInt(arc);
                }
            }
        });
    }
    
    protected Displayable content() {
        //TODO сюда отлично вольется геометри ридер
        return CG.Canvas(
                CG.ZStack(
                new Int(CG.ALIGNMENT_BOTTOM | CG.ALIGNMENT_LEFT),
                
//                new Arr(new CGDrawable[] {
//                    CG.Rect()
//                            .color(0xFFFFFF),
//                    CG.Rect()
//                            .color(0x0000FF),
//                    CG.Rect()
//                            .color(0xFF0000),
//                })

                    CG.Rect()
                            .color(0xFFFFFF)
                            .width(88).height(88),
                    CG.Rect()
                            .color(0x0000FF)
                            .width(77).height(77),
                    CG.Rect()
                            .color(0xFF0000)
                            .width(66).height(66)
                )
//                CG.Arc(30, 300)
//                        .color(arcColor)
//                        .setFrame(10, 10, 100, 100),
//                CG.If(cond,
//                    CG.Rect()
//                        .color(0x00FF00)
//                        .setFrame(75, 25, 10, 10),
//                    CG.Circle()
//                        .color(0xFF0000)
//                        .setFrame(75, 25, 10, 10)
//                )
                )
                .color(0x00FF00);
    }
}
