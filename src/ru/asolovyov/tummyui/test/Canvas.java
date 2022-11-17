/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.threading.Clock;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGSize;
import ru.asolovyov.tummyui.graphics.views.CGText;

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

    
    // TODO запилить движок анимаций: сделать здоровенный метод в CG, типа animate(delay, duration, view, frame, bgcolor, borderColor, cornerRadius, inset итп)
    // TODO сделать DisplayLink, это таймер который тикает раз в 33мс. В методе анимейт создавать объект анимации, который сушает дисплейЛинк и на каждый тик
    // меняет переданные параметры на некоторую дельту = дюрейщен/33

    // TODO Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
    //   Лайны, круги и прочие примитивы тоже должны сдвигаться относительно инсетов внутрь

    // TODO  СДЕЛАТЬ ЦГ-ФОР-ИЧ вьюху

     //TODO ЗАЕБЕНИТЬ СПЕЙСИНГ!
    protected Displayable content() {
        // TODO управление памятью,
        // TODO отписка от подписок,
        return CG.Canvas(
                CG.VStack(
                new Int(CG.TOP | CG.RIGHT),

//            CG.Image("res/spok.png"),
            //.height(90).width(101),
            
            CG.Text("Если будет много текста")
                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
                .color(0xFF0000)
                .backgroundColor(0x00FF00)
                .borderColor(0x0000FF)
                .cornerRaduis(new CGSize(20, 20))
                .width(100),

          CG.Text("он просто обрежется многоточием")
                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
                .color(0xFF0000)
                .backgroundColor(0x00FF00)
                .borderColor(0x0000FF)
                .cornerRaduis(new CGSize(20, 20))
                .width(100),

         CG.Text("как будто так и надо")
                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
                .color(0xFF0000)
                .backgroundColor(0x00FF00)
                .borderColor(0x0000FF)
                .cornerRaduis(new CGSize(20, 20))
                .width(100)
//                .height(70).width(100)
                
//                    CG.Rect()
//                            .height(230).width(30)
//                            .resizingMask(CGFrame.FLEXIBLE_WIDTH)
//                            .backgroundColor(0xFFFFFF),
//                    CG.Rect()
//                             .height(130).width(30)
//                            .resizingMask(CGFrame.FLEXIBLE_WIDTH)
//                            .backgroundColor(0x0000FF),
//                    CG.Rect()
//                             .height(30).width(30)
//                            .resizingMask(CGFrame.FLEXIBLE_WIDTH)
//                            .backgroundColor(0xFF0000)

               
//                CG.Text("Hello")
//                .handleKeyboard(new CGDrawable.KeyboardHandler() {
//                    public void keyPressed(CGDrawable self, int keyCode) {
//                        ((CGText)self).text("PRESSED: " + keyCode);
//                    }
//                })
//                .height(80).width(200)
//                .backgroundColor(0xFF0000),
//
//                CG.Text("Ololo")
//                .handleKeyboard(new CGDrawable.KeyboardHandler() {
//                    public void keyReleased(CGDrawable self, int keyCode) {
//                        ((CGText)self).text("RELEASED: " + keyCode);
//                    }
//                })
//                .height(80).width(200)
//                .backgroundColor(0x00FF00),
//
//                CG.Text("Trololo")
//                .handleKeyboard(new CGDrawable.KeyboardHandler() {
//                    public void keyRepeated(CGDrawable self, int keyCode) {
//                        ((CGText)self).text("REPEATED: " + keyCode);
//                    }
//                })
//                .height(80).width(200)
//                .backgroundColor(0x0000FF)

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
////                            .setOrigin(25, 25)
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
                )
                .setContentInset(10, 10, 10, 10) // TODO инсет внутри стека не работает
                )
                .backgroundColor(0xFFFFFF);
    }
}
