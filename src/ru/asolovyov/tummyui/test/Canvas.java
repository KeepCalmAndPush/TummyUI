/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.threading.Clock;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGColor;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;
import ru.asolovyov.tummyui.graphics.CGSize;
import ru.asolovyov.tummyui.graphics.views.CGStack;

/**
 * TODO тест-кейсы
 *
 * 1) Примитивы и фреймы
 * 2) Стеки
 * 3) Геометри ридер
 * 4) Чтение нажатия клавиш
 * 5) Тексты - с переносом и без
 * 6) РЕСАЙЗИНГ! Контейнер больше вьюхи -> растягивание/центрирование вьюхи
 *    Контейнер меньше вьюхи -> сжатие вьюхи/выезд вьюхи за границу контейнера(центрирование)
 *    Флексибл вьюхи и тянутся и сжимаются
 *    Гровабл тянутся
 *    Шринкабл сжимаются
 *    Фиксед остаются фиксед
 * @author Администратор
 */
public class Canvas extends UIMIDlet {
    protected Displayable content() {
        // TODO управление памятью,
        // TODO отписка от подписок,
        return CG.Canvas(
//                testVStackWithTwoViewsNonfixAndSecond20HFix() //OK
//                testVStackWithTwoViews20HFixAndSecondNonfix() //OK
//                testHStackWithTwoViewsNonfixAndSecond20WFix() //OK
//                testHStackWithTwoViews20WFixAndSecondNonfix() //OK
//                testVStackWithTwoViewsViewFillsCanvas() //OK
//                testHStackWithTwoViewsViewFillsCanvas() //OK
//                testVStackWithOneViewFillsCanvas() //OK
//                testZStackWithOneViewFillsCanvas() //OK
//                testZStackWithTwoViewsFillsCanvasAndRespectsOrder() //OK
//                testHStackWithOneViewFillsCanvas() //ок

                //OK НО ТУТ ЕСТЬ ТРАБЛЫ 1) Если не задать высоты текстам, то все растягивается даже в ширину
                // 2) Между ВСтеками в ХСтеке двойное расстояние.
                // 3) НЕ РАБОТАЕТ СКРОЛЛИНГ СТЕКА :(
                // 4) Оч тормозит
                textStylesIteratingHorizontalStackOfLabels() 
//                testFrameSetsByMaxWidthMaxHeight() //OK
//                testRectFillsCanvasWhenNoDimensionsSet() //OK
//                testRectFillsCanvasWhenSmallMinsSet() //OK
//                testRectFrameOk()//OK
          ).backgroundColor(CGColor.RED);
    }

    private CGDrawable testHStackWithTwoViews20WFixAndSecondNonfix() {
        return CG.HStack(
                CG.Rect().backgroundColor(CGColor.BLUE).width(20),
                CG.Rect().backgroundColor(CGColor.YELLOW))
                .backgroundColor(CGColor.ORANGE)
                ;
    }

    private CGDrawable testHStackWithTwoViewsNonfixAndSecond20WFix() {
        return CG.HStack(
                CG.Rect().backgroundColor(CGColor.BLUE),
                CG.Rect().backgroundColor(CGColor.YELLOW).width(20)
                )
                .backgroundColor(CGColor.ORANGE)
                ;
    }

    private CGDrawable testVStackWithTwoViews20HFixAndSecondNonfix() {
        return CG.VStack(
                CG.Rect().backgroundColor(CGColor.BLUE).height(20),
                CG.Rect().backgroundColor(CGColor.YELLOW))
                .backgroundColor(CGColor.ORANGE)
                ;
    }

    private CGDrawable testVStackWithTwoViewsNonfixAndSecond20HFix() {
        return CG.VStack(
                CG.Rect().backgroundColor(CGColor.BLUE),
                CG.Rect().backgroundColor(CGColor.YELLOW).height(20)
                )
                .backgroundColor(CGColor.ORANGE)
                ;
    }

    //OK
    private CGDrawable testVStackWithTwoViewsViewFillsCanvas() {
        return CG.VStack(
                CG.Rect().backgroundColor(CGColor.BLUE),
                CG.Rect().backgroundColor(CGColor.YELLOW)
                );
    }

    //OK
    private CGDrawable testHStackWithTwoViewsViewFillsCanvas() {
        return CG.HStack(
                CG.Rect().backgroundColor(CGColor.BLUE),
                CG.Rect().backgroundColor(CGColor.YELLOW)
                );
    }

    //OK
    private CGDrawable testHStackWithOneViewFillsCanvas() {
        return CG.HStack(
                CG.Rect().backgroundColor(CGColor.GREEN)
                )
                .backgroundColor(CGColor.ORANGE);
    }

    private CGDrawable testVStackWithOneViewFillsCanvas() {
        return CG.VStack(CG.Rect().backgroundColor(CGColor.GREEN));
    }

    private CGDrawable testZStackWithOneViewFillsCanvas() {
        return CG.ZStack(CG.Rect().backgroundColor(CGColor.GREEN));
    }

    private CGDrawable testZStackWithTwoViewsFillsCanvasAndRespectsOrder() {
        return CG.ZStack(
                CG.Rect().backgroundColor(CGColor.YELLOW)
                ,
                CG.Rect().backgroundColor(CGColor.BLUE)
                );
    }

//    private CGDrawable kek = CG.Rect()
//                .backgroundColor(CGColor.GREEN_YELLOW)
////                .frame(10, 10, 100, 100)
////                .width(100)
////                .height(100)
//                .maxWidth(132)
//                .maxHeight(176)
//                ;

//    int color = CGColor.WHITE_SMOKE;
    private CGDrawable testFrameSetsByMaxWidthMaxHeight() {
        /*
         * CGRectangle@f4819689 CGFrame@168c00 (0,0; 0,0) WILL SET WIDTH 100
            83 CVS sendValue 100
            84 CVS sendValue 100
            19 CVS sendValue ru.asolovyov.combime.bindings.Int@f188ac1a subscriptions: 0
            CGRectangle@f4819689 CGFrame@168c00 (0,0; 0,0) WILL SET HEIGHT 100
            85 CVS sendValue 100
            86 CVS sendValue 100
            22 CVS sendValue ru.asolovyov.combime.bindings.Int@e8b7ef9f subscriptions: 0
         */
//        clock = new Clock(100500);
//        clock.add(new Runnable() {
//            public void run() {
////                color -= 10;
//                kek
//                        .width(kek.width() + 1)
//                        .height(kek.height() + 1)
////                        .backgroundColor(kek.backgroundColor() - 10)
//                        ;
//            }
//        });

//        return kek;

        return CG.Rect()
                .backgroundColor(CGColor.GREEN)
                .maxWidth(132)
                .maxHeight(176)
                ;
    }

    private CGDrawable testRectFillsCanvasWhenNoDimensionsSet() {
        return CG.Rect()
                .backgroundColor(CGColor.GREEN)
                ;
    }

    private CGDrawable testRectFillsCanvasWhenSmallMinsSet() {
        return CG.Rect()
                .backgroundColor(CGColor.GREEN)
                .minWidth(100).minHeight(100)
                ;
    }

    private CGDrawable testRectFrameOk() {
        return CG.Rect()
                .backgroundColor(CGColor.GREEN)
                .frame(10, 10, 100, 100)
                ;
    }
    
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
//    {
//        clock = new Clock(500);
//        clock.add(new Runnable() {
//            public void run() {
//                int j = i++%2;
//                cond.setBool(j == 0);
//
//                if (j == 0) {
//                    int arc = arcColor.getInt();
//                    arcColor.setInt(backColor.getInt());
//                    backColor.setInt(arc);
//                }
//            }
//        });
//    }

    
    // TODO запилить движок анимаций: сделать здоровенный метод в CG, типа animate(delay, duration, view, frame, bgcolor, borderColor, cornerRadius, inset итп)
    // TODO сделать DisplayLink, это таймер который тикает раз в 33мс. В методе анимейт создавать объект анимации, который сушает дисплейЛинк и на каждый тик
    // меняет переданные параметры на некоторую дельту = дюрейщен/33

    /*
     * !!! ТЕСТ КЕЙСЫ !!!
     */

    private CGDrawable textStylesIteratingHorizontalStackOfLabels() {
        return CG.HStack(
                new CGStack(
                CGStack.AXIS_VERTICAL,
                new Object[]{new Integer(Font.SIZE_SMALL), new Integer(Font.SIZE_MEDIUM)},
                new CGStack.DrawableFactory() {
                    public CGDrawable itemFor(Object viewModel) {
                        int size = ((Integer) viewModel).intValue();
                        return CG.Text("12345")
                                //TODO CG.RIGHT выносит текст на ПОЛБУКВЫ БЛЕАТЬ ЗА ГРАНИЦУ ФРЕЙМА!
                                .alignment(CG.VCENTER | CG.RIGHT)
                                .font(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, size))
                                .color(0xFF0000)
                                .backgroundColor(0x00FF00)
                                .borderColor(0x0000FF)
                                .cornerRaduis(new CGSize(20, 20))
                                .width(50);
                    }
                }).spacing(15),
                        new CGStack(
                CGStack.AXIS_VERTICAL,
                new Object[]{
                    new Integer(Font.STYLE_PLAIN),
                    new Integer(Font.STYLE_UNDERLINED),
                    new Integer(Font.STYLE_BOLD)
                },
                new CGStack.DrawableFactory() {

                    boolean isEven = true;

                    public CGDrawable itemFor(Object viewModel) {
                        int style = ((Integer) viewModel).intValue();
                        isEven = !isEven;

                        return CG.Text("ABC")
                                .alignment(CG.CENTER)
                                .font(Font.getFont(Font.FACE_PROPORTIONAL, style, Font.SIZE_LARGE))
                                .color(isEven ? CGColor.RED : CGColor.BLACK)
                                .backgroundColor(isEven ? CGColor.GREEN : CGColor.WHITE)
                                .borderColor(isEven ? CGColor.BLUE : CGColor.RED)
                                .cornerRaduis(new CGSize(20, 20))
                                .width(50);
                    }
                })
                        .spacing(5)
                        )
                        .backgroundColor(CGColor.LIGHT_SKY_BLUE)
                        .borderColor(CGColor.BLUE)
                        .cornerRaduis(new CGSize(20, 20))
                        .maxHeight(160)
                        .maxWidth(160)
                        .x(10).y(10);
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