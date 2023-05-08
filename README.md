# TummyUI
Declarative UI framework for Java ME powered phones. Inspired by Apple's SwiftUI. Requires only MIDP 1.0/CLDC 1.1 so is compatible with most (if not all) of the phones. 

Uses [CombiME](https://github.com/KeepCalmAndPush/CombiME) - reactive programming framework for JavaME phones, inspired by Apple's Combine.

Due to limitations of the MIDP 1.0, TummyUI is split into two parts: UI-part (forms), to build user interface with standard components only, and CG-part (graphics), providing custom drawing and animating capabilities for your own layouts and controls.

## Note
The purpose of the project is to make a proof of concept that declarative layout is achievable even on older devices, when there was no trend of declarative reactive programming in mobile. This is not a strict implementation of all the possibilities of SwiftUI, rather than a syntactical imitation, aiming to feel classic mobile development as modern as possible. The title of the project derives from SwiftUI through J2MEUI ('ʤeɪ tuː miː ui') to TummyUI.

## UI Part (Forms)
The UI-part is split between two packages: `ru.asolovyov.tummyui.forms` (core infrastructural classes) and `ru.asolovyov.tummyui.forms.views` (UI components themselves).

![Снимок экрана 2023-05-07 в 12 44 49](https://user-images.githubusercontent.com/13520824/236673109-ce71da0a-a27d-4e61-acf8-1e5c92db64d9.png)

### Core
The entry point for all the TummyUI is the `UIMIDlet` class. Abstract descendant of `javax.microedition.midlet.MIDlet`, it requires to implement a single method: `protected abstract Displayable content();` which must return the first screen of your app. Also `UIMIDlet` notifies its listeners of MIDlet lifecycle events (start, pause, destroy) by virtue of CombiME's `PassthroughSubjects`. 

Further screens can be easily presented by using the navigation capabilities of TummyUI: see how `UIForm`s conform to `UINavigatable` or use a `UIDisplayableNavigationWrapper` to provide any `javax.microedition.lcdui.Displayable` as a TummyUI's navigatable object. 

One way of triggering the navigation is usage of `UICommand`s. These objects extend `javax.microedition.lcdui.Command` with inline handlers, which make the callback experiense more iOS and closure-like. 


Worth noting the `UIEnvironment` class. It works like an app-wide session where you can put your objects keyed by `String` identifiers. It also keeps a reference to a current `UIMIDlet`.

Finally, the `UI` class provides a set of static methods of instantiating Views, so you do not need to create them with `new` keyword, making the code looking more Swifty. Here is an example of a simple UI form, with navigation to TextBox and reactive handling of editing events:

```java
public class FormsTest extends UIMIDlet {

    protected Displayable content() {
        return UI.Form("Forms",
                    UI.StringItem(UIEnvironment.put("hello-world-key", "Hello, world!"))
               )
               .navigationCommand(
                    "Change it!", "Back",
                     UI.TextBox("UITextBox", UIEnvironment.string("hello-world-key"))
               );
    }

}
```
https://user-images.githubusercontent.com/13520824/236672918-4da2dc0c-8729-49f7-ba5b-d9046a056481.mov

### Views

Views in TummyUI consist of wrappers over standard J2ME components (Form, Image, TextBox etc) and add some new container classes: `UIIf`, `UIForEach` and `UIGroup`.

#### Containers
`UIForm` is a workhorse of all UI-layout. It is a subclass of `javax.microedition.lcdui.Form` and allows placing of other UI-components, represented by descendants of `UIItem`.
Each `UIItem` may become hidden/visible and raise a `needsRelayout` flag if some other change occured, that needs to be represented in layout. `UIIForm` listens to this changes reactively and redraws its contents if needed.

The simplest container class is `UIGroup` which holds an array of other `UIItem`s, so they can be added or removed from the interface at once.

`UIIf` lets you show or hide portions of the interface depending on a state of its `Bool` binding. `UIGroup` comes in handy here, allowing you to batch-control the visibility of components.

Finally, `UIForEach` lets you dynamically transform your reactive subject (`Arr`) of models into a list of `UIItem`s. This is possible by providing an instance of `UIGroup.ItemFactory`.

#### Components
Regular visual components are quite self-explanatory. In `ru.asolovyov.tummyui.forms.views` TummyUI has an assortment of `UIAlert`, `UIChoiceGroup`, `UIDateField`, `UIGauge`, `UIImageItem`, `UIStringItem`, `UITextField`, `UITextBox`. These classes wrap eponymous system components. Here is an extensive example of all available views. Left command button rises an Alert, right command triggers logic in If container. Note that If container manages two ForEach nested containers.

```java
public class FormsTest extends UIMIDlet {

    private Bool alertTrigger = new Bool(false);
    private Bool isOdd = new Bool(true);
    private Arr oddValues = new Arr(new Object[]{"1", "3"});
    private Arr evenValues = new Arr(new Object[]{"2", "4"});

    protected Displayable content() {
        return UI.Form("Forms",
                UI.If(isOdd)
                    .Then(
                        UI.ForEach(oddValues, new ItemFactory() {
                            public UIItem itemFor(Object viewModel) {
                                return UI.StringItem("Odd:", (String) viewModel);
                            }
                    })).Else(
                        UI.ForEach(evenValues, new ItemFactory() {
                            public UIItem itemFor(Object viewModel) {
                                return UI.StringItem("Even:", (String) viewModel);
                            }
                    })),
                UI.DateField("Today is:", DateField.DATE_TIME, new Date()),

                UI.Group(
                    UI.StringItem(UIEnvironment.put("hello-world-key", "Hello, world!")),
                    UI.TextField(UIEnvironment.string("hello-world-key")),
                    UI.Gauge("Gauge", true, 1, 10)),
                    UI.ChoiceGroup("Are you a", ChoiceGroup.EXCLUSIVE, new ListItem[]{
                        new ListItem("Cat person", null, true),
                        new ListItem("Dog person", null, false)
                }),

                UI.ImageItem(null, "res/1.png", 0, "Cat")
               )
               .alert(alertTrigger, UI.Alert("ALERT!", "Hello!", null, AlertType.ALARM))
               .command(new UICommand("Alert", new UICommand.Handler() {
                    public void handle() {
                        alertTrigger.setBool(!alertTrigger.getBoolean());
                    }
                }))
                .command(new UICommand("If", new UICommand.Handler() {
                    public void handle() {
                        isOdd.setBool(!isOdd.getBoolean());
                    }
                }));
    }
}
```
https://user-images.githubusercontent.com/13520824/236685997-0310aa6f-f8b3-4023-9623-8fffcb0d24fd.mov

## CG Part (Graphics)
CG Part of TummyUI provides some basic infrastructure in `ru.asolovyov.tummyui.graphics`, convenient bindings in `ru.asolovyov.tummyui.graphics.bindings` and the assortment of views and primitives in `ru.asolovyov.tummyui.graphics.views`.

### Core
The infrastructure consists of `CG` class with static methods (like `UI`) to eye-candify creation of CG-views omitting the `new` keyword. `CGColor` lists 100+ named colors as `int` constants, `CGFrame`, `CGInsets`, `CGPoint` and `CGSize` mimick eponymous data structures in iOS. Due to lack of Generics in JavaME, there are respective bindings for that structures: `Frame`, `Insets`, `Point` and `Size`.

`CGDisplayLink` - is a timer, claiming each frame of animation (TummyUI runs at 30 fps by default) and providing a capability to submit your own animations. 

Animations are implemented by subclassing the `CGAnimation` class. The actual animation happens in overriden method `protected abstract void animations(CGDrawable drawable);`. Set the final animatable values of your views inside this method. Currently only 9 properties of a `CGView` are animatable: `x` / `y` / `width` / `height`,  `cornerRadius`, colors: `color`, `backgroundColor`, `borderColor`, angle of CGArcs: `startAngle`. Animation may be one of three types: `SIMPLE` (runs once), `LOOP` (runs to the end, then hops onto the beginning and runs again, indefinetly), `AUTOREVERSE` (runs to the end, then animatedly reverses to the initial state, runs indefinetly). You may restart or abort animation with respective methods. Also you may provide a completion to be run after each run of animation by overriding the `protected void completion(CGAnimation animation)`. Here is an animation moving a square, meanwhile making it round a changing its color:

```java
private CGDrawable testAnimationOk() {
        CGDrawable rect = CG.Rect()
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
```

https://user-images.githubusercontent.com/13520824/236754400-25ed379a-f1d9-4a47-9628-75b1b9909ac1.mov

### Views
Drawing in CG part is done via `CGCanvas`. It is a descendant of `javax.microedition.lcdui.Canvas`, so it is a `Displayable` and may be returned as `content` of `UIMIDlet`. Canvas instantiates with one or several implementors of `CGDrawable`, a protocol defining a view in CG part or TummyUI. Canvas takes all the screen space and positions its views inside. 

Views, as said, must conform to `CGDrawable` protocol, defining crucial properties of a view: its position, size and flexibility, appearance like colors, borders and radius, and ability to listen to keyboard events (see `public CGDrawable handleKeyboard(KeyboardHandler handler)`) and reposition itself in bounds provided by a parent view ("reading the geometry" in terms of SwiftUI, see `public CGDrawable readGeometry(GeometryReader reader)`). For your own views, you are encouraged to subclass an abstract `CGSomeDrawable`, which has implemented all beforementioned capabilities.

Also, there is another view-related protocol: `CGFontSupporting`, which extends `CGDrawable` and finds it usage only in `CGText` (think of it as Text in SwiftUI).

Let's take a brief overview on basic view-primitives in TummyUI.

* `CGArc` is a basic component to draw stroked or filled arcs and circles, specifying its `startAngle` and `endAngle`
* `CGLine` a primitive of a line, which draws diagonally between top-left and bottom-right corners of its enclosing frame. Or top-right to bottom-left if it is `inverted`.
<img width="255" alt="Снимок экрана 2023-05-08 в 10 36 50" src="https://user-images.githubusercontent.com/13520824/236764282-1ca310ab-d763-489d-9437-b2a74a55b6b7.png">

```java
private CGDrawable testLine() {
        final CGLine line1 = (CGLine) CG.Line()
                    .strokeWidth(10)
                    .backgroundColor(CGColor.WHITE)
                    .color(CGColor.GREEN)
                    .width(50)
                    .height(50);
        
        CGLine line2 = (CGLine) CG.Line()
                    .isInverted(true)
                    .strokeWidth(10)
                    .backgroundColor(CGColor.WHITE)
                    .color(CGColor.GREEN)
                    .width(50)
                    .height(50);

        return CG.HStack(
                line1,
                line2
                ).spacing(10);
    }
```

* `CGImage` accepts a `javax.microedition.lcdui.Image` or `Obj` binding of said images to display an image.
* `CGRectangle` is the simplest concrete descendant of `CGSomeDrawable`, consider it as iOS `UIView`. Here is a filled rectangle, with thick rounded borders and a shadow:
![image](https://user-images.githubusercontent.com/13520824/236768089-83401e6f-84b6-4264-89c8-850bbe2b6ec3.png)
(Note that due to lack of native antialiasing and floating point calculations in JavaME, there are some non-painted dots 'looking through').

```
                CG.Rect()
                  .width(150).height(50)
                  .shadowColor(CGColor.GRAY)
                  .shadowOffset(5, 5)
                  .cornerRadius(20)
                  .backgroundColor(CGColor.RED)
                  .borderColor(CGColor.BLUE)
                  .borderWidth(10)
                )
```

* `CGText` works as a `Text` or `UILabel` in terms of iOS. Is multiline, has its font and alignment.
* `CGIf` works like `UIIf`, switching between `CGDrawable`s in accordance to its `Bool` predicate.
* `CGPattern` lets you draw a pattern tile-by-tile. Subclass the `CGPattern` and implement its `public abstract void drawTile(Graphics g, CGFrame frame)` method. Here is an exemple of a checkerdoard pattern:
<img width="248" alt="Снимок экрана 2023-05-08 в 11 04 32" src="https://user-images.githubusercontent.com/13520824/236770184-c502ca6a-28a4-438d-b4c1-e7876c6288c9.png">

```java
private CGDrawable testPattern() {
        CGPattern pattern = new CGPattern() {
            public void drawTile(Graphics g, CGFrame frame) {
                g.setColor(CGColor.WHITE);
                g.fillRect(frame.x, frame.y, frame.width, frame.height);

                int h = frame.height / 2;

                g.setColor(CGColor.BLACK);
                g.fillRect(frame.x, frame.y, h, h);
                g.fillRect(frame.x + h, frame.y + h, h, h);
            }
        };

        return CG.ZStack(
                pattern
                    .tileSize(new CGSize(32, 32))
                );
    }
```

### Layout
TummyUI was designed with great simplification of layout process in mind. Basically, `CGDrawable`s are positioned on canvas according to their `frame`s. Nothing stops you from filling your Canvas with this raw approach. But where TummyUI really shines, is usage of Stacks. TummyUI provides a `CGStack` class which can have customized `axis` to be either horizontal, vertical or Z. You may change this setting in runtime and all your views inside a stack will be relayouted accordingly. `CGStack`s are scrollable by default, sor you will never miss a bit of your content even on devices with small and unusual screen resolutions.

Stacks will distribute your views according the stacks' axis, alignment and views' `flexibility` and `intrinsicContentSize`.

Also you may set min and max (`minX`, `minY`, `minWitdh`, `minHeight`, `maxX`, `maxY`, `maxWitdh`, `maxHeight`) and this will be taken into account too. Look at this examples:

1. A vertical stack with Yellow view's height fixed at 60px. The Green view occupies the rest of the stack:
<img width="247" alt="Снимок экрана 2023-05-08 в 11 22 41" src="https://user-images.githubusercontent.com/13520824/236773943-28f1832f-a9a2-48a5-8767-6540118d72ff.png">

```java
private CGDrawable testVStackWithTwoViewsNonfixAndSecond60HFix() {
        return CG.VStack(
                CG.Rect().backgroundColor(CGColor.GREEN),
                CG.Rect().backgroundColor(CGColor.YELLOW).height(60)
                )
                .backgroundColor(CGColor.ORANGE)
                ;
    }
```

2. A horizontal stack with Red view no wider than 50px, Blue view no narrower than 20px and a free-sized Green view. The stack distributes its space between the children, respecting the Red view's max width of 50 and giving Blue view a width of 50 too, because no flexibility priorities were specified. And gives the rest to the Green view.
<img width="250" alt="Снимок экрана 2023-05-08 в 11 31 14" src="https://user-images.githubusercontent.com/13520824/236775903-824fc03d-7d0f-4cb3-8828-48ceb4542ac5.png">

```java
private CGDrawable testRGB() {
         return CG.HStack(
                CG.Rect().backgroundColor(CGColor.RED)
                    .maxWidth(50),
                CG.Rect().backgroundColor(CGColor.GREEN),
                CG.Rect().backgroundColor(CGColor.BLUE)
                    .minWidth(20)
                );
    }
```

3. The same as above, but now Red view has a minimal width of 20 and Blue view has a significant resizing priority. So the Red view is flatten down to it's 20px, the Green view is consumed entirely and the Blue view takes virtually all the space it can:
<img width="251" alt="Снимок экрана 2023-05-08 в 11 41 39" src="https://user-images.githubusercontent.com/13520824/236778150-b5d41578-11d7-45cf-8057-9b12d47d6e8b.png">

```java
private CGDrawable testRGB() {
         return CG.HStack(
                CG.Rect().backgroundColor(CGColor.RED)
                    .minWidth(20)
                    .maxWidth(50),
                CG.Rect().backgroundColor(CGColor.GREEN),
                CG.Rect().backgroundColor(CGColor.BLUE)
                    .minWidth(20)
                    .flexibilityWidth(200)
                );
    }
```

4. Stacks can be easily nested and aligned. The follwing illustrates this, as well as the ability of stacks and views calculate respective intrinsic sizes. Consider this is a control for changing the locale of text being entered: Numerics, Russian or English letters:
<img width="245" alt="Снимок экрана 2023-05-08 в 11 54 24" src="https://user-images.githubusercontent.com/13520824/236781297-d3f83bc9-2b57-4845-9145-fdbe915e3d99.png">

```java
private CGDrawable testLanguageTopRightUI() {
        return CG.ZStack(
                CG.Rect().backgroundColor(CGColor.YELLOW),

                CG.HStack(
                        CG.Text("123|RU|EN")
                        .alignment(CG.CENTER)
                        .color(CGColor.WHITE)
                        .backgroundColor(CGColor.GREEN)
                        .flexibility(CGDrawable.FLEXIBILITY_ALL_NONE)
                    )
                    .alignment(CG.TOP | CG.RIGHT)
                    .borderColor(CGColor.PINK).borderWidth(5)
                )
                ;
    }
```

5. Stacks (akeen to `UIForEach`) can use `DrawableFactories` to generate the content provided as an `Arr` of view models. Take a look at two VStacks inside an HStack, generating `CGTexts` of different font attributes:
<img width="252" alt="Снимок экрана 2023-05-08 в 12 07 38" src="https://user-images.githubusercontent.com/13520824/236784323-220dd9e4-d592-4db7-ad9a-6618b5b71811.png">

```java
private CGDrawable textStylesIteratingHorizontalStackOfLabels() {
        return CG.HStack(
                CG.VStack(
                    new Object[]{new Integer(Font.SIZE_SMALL), new Integer(Font.SIZE_MEDIUM)},
                    new CGStack.DrawableFactory() {
                        public CGDrawable itemFor(Object viewModel) {
                            int size = ((Integer) viewModel).intValue();
                            return CG.Text("12345").alignment(CG.VCENTER | CG.RIGHT)
                                    .font(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, size))
                                    .color(0xFF0000)
                                    .backgroundColor(0x00FF00)
                                    .borderColor(0x0000FF)
                                    .cornerRadius(20)
                                    .width(50);
                        }
                })
                .spacing(15),

                CG.VStack(
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
                                    .borderColor(isEven ? CGColor.GREEN : CGColor.RED)
                                    .cornerRadius(10).width(50).height(30);
                            }
                    })
                    .spacing(5)
              )
              .spacing(10)
              .backgroundColor(CGColor.LIGHT_SKY_BLUE)
              .cornerRadius(20)
              .maxHeight(160)
              .maxWidth(160)
              .x(10)
              .y(10);
    }
```

6. Also you may construct complex layouts like chat feeds and custom controls like loaders and switchers with TummyUI:

https://user-images.githubusercontent.com/13520824/236788429-c0b051f1-e723-4487-b144-eee80a650cac.mov
https://user-images.githubusercontent.com/13520824/236788520-04d7b192-2a09-4ed9-bc8c-40f4b38d8c7b.mov
https://user-images.githubusercontent.com/13520824/236788576-2b5412e7-4dcf-4a75-b837-b3a88df0e6c7.mov


## Final notes
Consult `Canvas` and `FormsTest` MIDlets for more samples. Feel free to fork and file merge requests :)
