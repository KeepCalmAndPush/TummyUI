/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.test;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGAnimation;
import ru.asolovyov.tummyui.graphics.CGColor;
import ru.asolovyov.tummyui.graphics.CGPoint;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;
import ru.asolovyov.tummyui.graphics.views.CGCanvas;
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
    private int testScreenIndex = 0;

    //TODO РАЗОБРАТЬСЯ ПОЧЕМУ З_СТЭК ДЕЛИТ РАЗМЕРЫ МЕЖДУ ВСЕМИ ВЬЮХАМИ
    //TODO СДЕЛАТЬ ПАБЛИШЕРЫНЙ МЕТОД REPLACE/PIPE
    private Object[] testScreens = new Object[] {
          testLanguageTopRightUI()
//        testThickBordersInsideZStack(), //ok
//        testZSTextTitleAndRectContent(),//ok
//        testThickBorders(), //  OK но скругления дырявые
//        testShadows(), //ОК
//                testVSTextTitleAndRectContent(), // OK
//                testVSTextTitleAndHStackContent(), //OK,
//
//
//                testAnimationOk(),
//                testAnimationYellowTrip(),
//                testRectFrameAndCornerRadiusOk(),//OK
//
//                // OK НО ТУТ ЕСТЬ ТРАБЛЫ 1) Если не задать высоты текстам, то все растягивается даже в ширину
//                // 3) НЕ РАБОТАЕТ СКРОЛЛИНГ СТЕКА :(
//                // 4) Оч тормозит
//                textStylesIteratingHorizontalStackOfLabels(), // :(
//                testVStackWithTwoViewsNonfixAndSecond20HFix(), //OK
//                testVStackWithTwoViews20HFixAndSecondNonfix(), //OK
//                testHStackWithTwoViewsNonfixAndSecond20WFix(), //OK
//                testHStackWithTwoViews20WFixAndSecondNonfix(), //OK
//                testVStackWithTwoViewsViewFillsCanvas(), //OK
//                testHStackWithTwoViewsViewFillsCanvas(), //OK
//                testVStackWithOneViewFillsCanvas(), //OK
//                
//                testZStackWithTwoViewsFillsCanvasAndRespectsOrder(), //OK
//                testHStackWithOneViewFillsCanvas(), //ок
//                testFrameSetsByMaxWidthMaxHeight(), //OK
//                testRectFillsCanvasWhenNoDimensionsSet(), //OK
//                testRectFillsCanvasWhenSmallMinsSet(), //OK
    };


    private CGDrawable testLanguageTopRightUI() {
        return CG.ZStack(
                    CG.HStack(
                        CG.Rect()
                            .backgroundColor(CGColor.BLUE),
                        CG.Rect()
                            .backgroundColor(CGColor.YELLOW)
                    ),

                    CG.HStack(
                        CG.Text("123|RU|EN")
                        .alignment(CG.RIGHT)
                        .color(CGColor.BLUE)
                        .backgroundColor(CGColor.GREEN)
                        .height(100)
                        .flexibilityWidth(0)
                    )
                    .alignment(CG.TOP | CG.RIGHT)
                    .borderColor(CGColor.BLACK)
                )
                ;
    }

    private CGDrawable testThickBordersInsideZStack() {
        return CG.ZStack(
                CG.Rect()
                  .width(150).height(50)
                  .shadowColor(CGColor.GRAY).shadowOffset(5, 5)
                  .cornerRadius(20)

                  .backgroundColor(CGColor.RED)
                  .borderColor(CGColor.BLUE).borderWidth(10)
                )
                .alignment(CG.LEFT | CG.VCENTER)
                .backgroundColor(CGColor.WHITE)
                ;
    }


    private CGDrawable testThickBorders() {
        // TODO ЕСЛИ Z-STACK то КОНТЕНТ АНИМИРОВАННО КУДА-ТО УЛЕТАЕТ!
        return CG.HStack(
                CG.Rect()
                  .width(150).height(50)
                  .shadowColor(CGColor.GRAY).shadowOffset(5, 5)
                  .cornerRadius(20)

                  .backgroundColor(CGColor.RED)
                  .borderColor(CGColor.BLUE).borderWidth(10)
                )
                .backgroundColor(CGColor.WHITE)
                ;
    }

    private CGDrawable testShadows() {
        return CG.VStack(
                    CG.HStack(
                        CG.Rect()
                            .width(50).height(50)
                            .shadowColor(CGColor.BLACK)
                            .shadowOffset(5, 5)
                            .backgroundColor(CGColor.RED)
                            .cornerRadius(10)
                            ,
                        CG.Rect()
                            .width(50).height(50)
                            .shadowColor(CGColor.BLUE)
                            .shadowOffset(-5, 15)
                            .backgroundColor(CGColor.GREEN)
                    ).spacing(20)
                     .borderColor(CGColor.BLACK),

                    CG.HStack(
                        CG.Rect()
                            .width(50).height(50)
                            .shadowColor(CGColor.GRAY)
                            .shadowOffset(5, -5)
                            .backgroundColor(CGColor.BLUE),
                        CG.Rect()
                            .width(50).height(50)
                            .shadowColor(CGColor.PINK)
                            .shadowOffset(-5, -5)
                            .backgroundColor(CGColor.BLACK)
                    )
                    .spacing(20)
                    .borderColor(CGColor.RED)
                )
                .backgroundColor(CGColor.WHITE)
                ;
    }
    
    private CGDrawable testVSTextTitleAndRectContent() {
        return CG.VStack(
                    CG.Text("TITLE")
                        .alignment(CG.CENTER)
                        .font(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE))
                        .flexibilityHeight(100)
                        .backgroundColor(CGColor.WHITE),
                    CG.Rect()
                        .flexibilityHeight(99)
                        .minHeight(50)
                        .backgroundColor(CGColor.GREEN)
                ).backgroundColor(CGColor.ORANGE)
                ;
    }

    private CGDrawable testVSTextTitleAndHStackContent() {
        return CG.VStack(
                CG.Text("TITLE")
                    .alignment(CG.CENTER)
                    .backgroundColor(CGColor.WHITE),
                CG.HStack(
                    CG.Rect().backgroundColor(CGColor.BLUE),
                    CG.Rect().backgroundColor(CGColor.YELLOW)
                )
                )
                .backgroundColor(CGColor.ORANGE)
                ;
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

    private CGDrawable testFrameSetsByMaxWidthMaxHeight() {
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

    private CGDrawable testRectFrameAndCornerRadiusOk() {
        return CG.Rect()
                .backgroundColor(CGColor.GREEN)
                .frame(10, 10, 100, 100)
                .cornerRadius(50)
                ;
    }

    //TODO ОЧЕНЬ МНОГО КОДА. НАДО СДЕЛАТЬ АНИМАЦИЮ ПРОСТО СТРУКТУРОЙ ДАННЫХ И ЧЕЙНИТЬ ИХ ЧЕРЕЗ THEN/DELAY
    private CGDrawable testAnimationYellowTrip() {
        CGDrawable rect =  CG.Rect()
                .backgroundColor(CGColor.YELLOW)
                .frame(10, 10, 50, 50)
                .animate(new CGAnimation(1000) {
                    protected void animations(CGDrawable drawable) {
                        drawable.x(110);
                    }
                    protected void completion(final CGAnimation animation) {
                        animation.getDrawable().animate(new CGAnimation(1000) {
                            final CGAnimation parentAnimation = animation;
                            protected void animations(CGDrawable drawable) {
                                drawable.y(110);
                            }
                            
                            protected void completion(CGAnimation animation) {
                                animation.getDrawable().animate(new CGAnimation(1000) {
                                    protected void animations(CGDrawable drawable) {
                                        drawable.x(10);
                                    }

                                    protected void completion(CGAnimation animation) {
                                        animation.getDrawable().animate(new CGAnimation(1000) {
                                            protected void animations(CGDrawable drawable) {
                                                drawable.y(10);
                                            }
                                            
                                            protected void completion(CGAnimation animation) {
                                                parentAnimation.restart();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    })
                ;

        return rect;
    }

    private CGDrawable testAnimationOk() {
        CGDrawable rect =  CG.Rect()
                .backgroundColor(CGColor.YELLOW)
                .frame(10, 10, 50, 50)
                .animate(new CGAnimation(3000, CGAnimation.AUTOREVERSE) {
                    protected void animations(CGDrawable drawable) {
                        drawable
                                .x(80).y(88).width(100).height(100)
                                .cornerRadius(50)
                                .backgroundColor(CGColor.BLUE);
                }})
                ;

        return rect;
    }
    
//    Clock clock;
//    int i = 0;
//    Int arcColor = new Int(0xFFFF00);
//    Int backColor = new Int(0x0000FF);
//    Bool cond = new Bool(false);

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
                                    .cornerRadius(20)
                                    .width(50);
                         }
                }).spacing(15)
                  .borderColor(CGColor.ORANGE).borderWidth(3)
//                  .cornerRadius(10)
                  ,
                  
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
                                .cornerRadius(10)
                                .width(50)
                                .height(30);
                    }
                })
                        .spacing(5)
                        .borderColor(CGColor.BLUE_VIOLET).borderWidth(3)
//                        .cornerRadius(10)
                        )
                        .spacing(10)
                        .backgroundColor(CGColor.LIGHT_SKY_BLUE)
                        .borderColor(CGColor.BLUE)
                        .cornerRadius(20)
                        .maxHeight(160)
                        .maxWidth(160)
                        .x(10).y(10);
    }

    protected Displayable content() {
        // TODO управление памятью,
        // TODO отписка от подписок,
        final CGCanvas canvas =
                CG.Canvas(
                    (CGDrawable) testScreens[testScreenIndex]
                ).backgroundColor(CGColor.RED);

        return canvas.handleKeyboard(new CGDrawable.KeyboardHandler() {
            public void keyReleased(CGDrawable alwaysNull, int keyCode) {
                int index = testScreenIndex;
                S.debugln("INDEX WAS: " + index);
                if (keyCode == CG.KEY_LEFT) {
                    index -= 1;
                    if (index < 0) {
                        index = testScreens.length - 1;
                    }
                } else if (keyCode == CG.KEY_RIGHT) {
                    index += 1;
                    if (index == testScreens.length) {
                        index = 0;
                    }
                }
                S.debugln("INDEX NOW: " + index);
                if (index == testScreenIndex) {
                    S.debugln("INDEX SAME, BREAK");
                    return;
                }
                S.debugln("INDEX RENEWED PROCEED");
                testScreenIndex = index;
                // TODO ВЫСОТА ТЕКСТА СЧИТАЕТСЯ НЕПОЙМИ КАК
                // ТЕКСТ ПОКАЗЫВАЕТСЯ НЕ ВСЕГДА
                // ИНОГДА ТЕКСТ НЕ НА ВСЮ ШИРИНУ
//                CGDrawable testScreen = CG.VStack(
//                        CG.Text("Заголовок теста " + index)
//                        .color(CGColor.BLACK)
//                        .backgroundColor(CGColor.WHITE)
//                        ,
//
//                        ((CGDrawable) testScreens[testScreenIndex]).height(188)
//                        );
                CGDrawable testScreen = (CGDrawable) testScreens[testScreenIndex];
                canvas.setDrawable(testScreen);
            }
        });
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
//                .cornerRadius(new CGSize(20, 20))
//                .width(40),
//
//          CG.Text("он просто обрежется многоточием")
//                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
//                .color(0xFF0000)
//                .backgroundColor(0x00FF00)
//                .borderColor(0x0000FF)
//                .cornerRadius(new CGSize(20, 20))
//                .width(40),
//
//         CG.Text("как будто так и надо")
//                //Сделать чтобы все вьюхи двигали контент внутри себя: то есть уважали контент инсеты
//                .color(0xFF0000)
//                .backgroundColor(0x00FF00)
//                .borderColor(0x0000FF)
//                .cornerRadius(new CGSize(20, 20))
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
//                                   S.debugln("333333333");
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
//                                    S.debugln("222222222");
//                                    self.frame().width = frame.width / 2;
//                                }
//                             }),
//                    CG.Rect()
//                            .color(0xFF0000)
//                            .height(66)
//                            .flexibility(CGFrame.FLEXIBLE_ORIGIN)
//                            .readGeometry(new CGDrawable.GeometryReader() {
//                                public void read(CGDrawable self, CGFrame frame) {
//                                   S.debugln("666666666");
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