/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.threading.Clock;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGDrawable;
import ru.asolovyov.tummyui.graphics.CGText;

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

    /*
     CG.HStack(new GeometryReader() {
        public CGDrawable content(Obj frame) {
              return CG.Rectangle().setFrame(
              frame.map { onValue(CGFrame value) { return value.width / 3 } }
              );
        }
     }
     )
     *
     */
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
        // TODO управление памятью,
        // TODO отписка от подписок,
        // TODO синканье сабжектов
        // TODO Не заменять новыми сабжектами, а синкать в текущие
        return CG.Canvas(
                CG.VStack(
                new Int(CG.ALIGNMENT_CENTER),
                
                CG.Text("Hello")
                .handleKeyboard(new CGDrawable.KeyboardHandler() {
                    public void keyPressed(CGDrawable self, int keyCode) {
                        ((CGText)self).text("PRESSED: " + keyCode);
                    }
                })
                .height(80).width(200)
                .backgroundColor(0xFF0000),
                        
                CG.Text("Ololo")
                .handleKeyboard(new CGDrawable.KeyboardHandler() {
                    public void keyReleased(CGDrawable self, int keyCode) {
                        ((CGText)self).text("RELEASED: " + keyCode);
                    }
                })
                .height(80).width(200)
                .backgroundColor(0x00FF00),

                CG.Text("Trololo")
                .handleKeyboard(new CGDrawable.KeyboardHandler() {
                    public void keyRepeated(CGDrawable self, int keyCode) {
                        ((CGText)self).text("REPEATED: " + keyCode);
                    }
                })
                .height(80).width(200)
                .backgroundColor(0x0000FF)
//                    CG.Rect()
//                            .height(132)
//                            .resizingMask(CGFrame.FLEXIBLE_WIDTH)
//                            .color(0xFFFFFF),
//                    CG.Rect()
//                            .height(88)
//                            .resizingMask(CGFrame.FLEXIBLE_WIDTH)
//                            .color(0x0000FF),
//                    CG.Rect()
//                            .height(44)
//                            .resizingMask(CGFrame.FLEXIBLE_WIDTH)
//                            .color(0xFF0000)
//                new Arr(new CGDrawable[] {
//                    CG.Rect()
//                            .color(0xFFFFFF),
//                    CG.Rect()
//                            .color(0x0000FF),
//                    CG.Rect()
//                            .color(0xFF0000),
//                })

//                    CG.Rect()
//                            .color(0xFFFFFF)
//                            .height(48)
//                            .resizingMask(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                    S.println("333333333");
//                                    self.getCGFrame().width = frame.width / 3;
//                                }
//                             }),
//                    CG.Rect()
//                            .color(0x0000FF)
////                            .setOffset(25, 25)
//                            .height(77)
//                            .resizingMask(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                    S.println("222222222");
//                                    self.getCGFrame().width = frame.width / 2;
//                                }
//                             }),
//                    CG.Rect()
//                            .color(0xFF0000)
//                            .height(66)
//                            .resizingMask(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                    S.println("666666666");
//                                    self.getCGFrame().width = frame.width / 6;
//                                }
//                             })
//                )
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
                ))
                .backgroundColor(0x00AABB);
    }
}
