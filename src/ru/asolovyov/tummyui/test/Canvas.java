/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.threading.Clock;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGColor;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGSize;
import ru.asolovyov.tummyui.graphics.views.CGStack;
import ru.asolovyov.tummyui.graphics.views.CGText;

/**
 * TODO тест-кейсы
 *
 * 1) Примитивы и фреймы
 * 2) Стеки
 * 3) Геометри ридер
 * 4) Чтение нажатия клавиш
 * 5) Тексты - с переносом и без
 * 6) РЕСАЙЗИНГ МАСКИ! Контейнер больше вьюхи -> растягивание/центрирование вьюхи
 *    Контейнер меньше вьюхи -> сжатие вьюхи/выезд вьюхи за границу контейнера(центрирование)
 *    Флексибл вьюхи и тянутся и сжимаются
 *    Гровабл тянутся
 *    Шринкабл сжимаются
 *    Фиксед остаются фиксед
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
              return CG.Rectangle().frame(
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
    
    protected Displayable content() {
        // TODO управление памятью,
        // TODO отписка от подписок,
        return CG.Canvas(
                this.textStylesIteratingHorizontalStackOfLabels()
          ).backgroundColor(0xFFFFFF);
    }


    /*
     * !!! ТЕСТ КЕЙСЫ !!!
     */

    private CGDrawable textStylesIteratingHorizontalStackOfLabels() {
        return CG.HStack(
                new Int(CG.CENTER),
                new CGStack(
                CGStack.AXIS_VERTICAL,
                new Object[]{ new Integer(Font.SIZE_SMALL), new Integer(Font.SIZE_MEDIUM) },
                new CGStack.DrawableFactory() {
                    public CGDrawable itemFor(Object viewModel) {
                        int size = ((Integer) viewModel).intValue();
                        return CG.Text("12345")
                                .font(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, size))
                                .color(0xFF0000)
                                .backgroundColor(0x00FF00)
                                .borderColor(0x0000FF)
                                .cornerRaduis(new CGSize(20, 20))
                                .width(50);
                    }
                })
//                .origin(10, 20),
//
//
                //TODO Шстек Встеков рисуется где-то в миллионах световых лет
                //TODO Видимо ориджин и/или сайз не просетывается для стека, он рисуется всегда по центру экрана
                , new CGStack(
                    CGStack.AXIS_VERTICAL,

                    new Object[]{
                        new Integer(Font.STYLE_PLAIN),
                        new Integer(Font.STYLE_UNDERLINED),
                        new Integer(Font.STYLE_BOLD)
                },

                    new CGStack.DrawableFactory() {
                    boolean isEven = true;

                    public CGDrawable itemFor(Object viewModel) {
                        int style = ((Integer)viewModel).intValue();
                        isEven = !isEven;

                        return CG.Text("ABC")
                                .alignment(CG.CENTER)
                                .font(Font.getFont(Font.FACE_PROPORTIONAL, style, Font.SIZE_LARGE))
                                .color(isEven ? CGColor.RED : CGColor.BLACK)
                                .backgroundColor(isEven ? CGColor.GREEN: CGColor.WHITE)
                                .borderColor(isEven ? CGColor.BLUE : CGColor.RED)
                                .cornerRaduis(new CGSize(20, 20))
                                .width(50);
                    }
                })
//
//                //.origin(70, 20) // НЕ ПАШЕТ
//
//                        , new CGStack(
//                    CGStack.AXIS_VERTICAL,
//                    new Object[]{ new Integer(Font.FACE_MONOSPACE), new Integer(Font.FACE_SYSTEM), new Integer(Font.FACE_PROPORTIONAL)},
//                    new CGStack.DrawableFactory() {
//
//                    public CGDrawable itemFor(Object viewModel) {
//                        int face = ((Integer)viewModel).intValue();
//
//                        return CG.Text("Текст")
//                                .font(Font.getFont(face, Font.STYLE_PLAIN, Font.SIZE_LARGE))
//                                .color(0xFF0000)
//                                .backgroundColor(0x00FF00)
//                                .borderColor(0x0000FF)
//                                .cornerRaduis(new CGSize(20, 20))
//                                .width(50);
//                    }
//                })

                )
                .backgroundColor(CGColor.PINK)
                .borderColor(CGColor.RED)
                .cornerRaduis(new CGSize(10, 5))
                .origin(20, 20); // TODO НЕ РАБОТАЕТ!
    }
}


/*
 * Однажды я научусь делать все аккуратно:
 * //                .origin(130, 20)s
//                        )
//            CG.Image("res/spok.png"),
            //.height(90).width(101),

//            CG.Text("Если будет много текста")
//                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
//                .color(0xFF0000)
//                .backgroundColor(0x00FF00)
//                .borderColor(0x0000FF)
//                .cornerRaduis(new CGSize(20, 20))
//                .width(40),
//
//          CG.Text("он просто обрежется многоточием")
//                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
//                .color(0xFF0000)
//                .backgroundColor(0x00FF00)
//                .borderColor(0x0000FF)
//                .cornerRaduis(new CGSize(20, 20))
//                .width(40),
//
//         CG.Text("как будто так и надо")
//                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
//                .color(0xFF0000)
//                .backgroundColor(0x00FF00)
//                .borderColor(0x0000FF)
//                .cornerRaduis(new CGSize(20, 20))
//                .width(40)
//                .height(70).width(100)

//                    CG.Rect()
//                            .height(230).width(30)
//                            .flexibility(CGFrame.FLEXIBLE_WIDTH)
//                            .backgroundColor(0xFFFFFF),
//                    CG.Rect()
//                             .height(130).width(30)
//                            .flexibility(CGFrame.FLEXIBLE_WIDTH)
//                            .backgroundColor(0x0000FF),
//                    CG.Rect()
//                             .height(30).width(30)
//                            .flexibility(CGFrame.FLEXIBLE_WIDTH)
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
//                            .flexibility(CGFrame.FLEXIBLE_WIDTH)
//                            .color(0xFFFFFF),
//                    CG.Rect()
//                            .height(88)
//                            .flexibility(CGFrame.FLEXIBLE_WIDTH)
//                            .color(0x0000FF),
//                    CG.Rect()
//                            .height(44)
//                            .flexibility(CGFrame.FLEXIBLE_WIDTH)
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
//                            .flexibility(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                    S.println("333333333");
//                                    self.frame().width = frame.width / 3;
//                                }
//                             }),
//                    CG.Rect()
//                            .color(0x0000FF)
////                            .origin(25, 25)
//                            .height(77)
//                            .flexibility(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                    S.println("222222222");
//                                    self.frame().width = frame.width / 2;
//                                }
//                             }),
//                    CG.Rect()
//                            .color(0xFF0000)
//                            .height(66)
//                            .flexibility(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                    S.println("666666666");
//                                    self.frame().width = frame.width / 6;
//                                }
//                             })
//                )
//                CG.Arc(30, 300)
//                        .color(arcColor)
//                        .frame(10, 10, 100, 100),
//                CG.If(cond,
//                    CG.Rect()
//                        .color(0x00FF00)
//                        .frame(75, 25, 10, 10),
//                    CG.Circle()
//                        .color(0xFF0000)
//                        .frame(75, 25, 10, 10)
//                )
//                ).contentInset(10, 10, 10, 10)
 */