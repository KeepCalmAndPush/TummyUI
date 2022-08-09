/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;

/**
 *
 * @author Администратор
 */
public class CG {
    //TODO возможно алайменты надо сделать маской
    public final static int ALIGNMENT_CENTER = 0;
    public final static int ALIGNMENT_LEFT = 1;
    public final static int ALIGNMENT_RIGHT = 2;
    public final static int ALIGNMENT_TOP = 3;
    public final static int ALIGNMENT_BOTTOM = 4;
    
    public static CGArc Arc(int startAngle, int endAngle) {
        return new CGArc()
                .startAngle(new Int(startAngle))
                .endAngle(new Int(endAngle));
    }

    public static CGArc Circle() {
        return Arc(0, 360);
    }

    public static CGImage Image(Image image) {
        return new CGImage()
                .image(new Obj(image));
    }

    public static CGLine Line() {
        return new CGLine();
    }

    public static CGRectangle Rect() {
        return new CGRectangle();
    }

    public static CGIf If(boolean condition, CGDrawable ifItem, CGDrawable elseItem) {
        return new CGIf(new Bool(condition), ifItem, elseItem);
    }

    public static CGIf If(Bool condition, CGDrawable ifItem, CGDrawable elseItem) {
        return new CGIf(condition, ifItem, elseItem);
    }

    public static CGText Text(Str string) {
        return new CGText(string);
    }

    public static CGText Text(String string) {
        return new CGText(new Str(string));
    }

    public static CGCanvas Canvas(CGDrawable content) {
        return new CGCanvas(content);
    }

    public static CGCanvas Canvas(CGDrawable c1, CGDrawable c2) {
        return new CGCanvas(new CGDrawable[] { c1, c2 });
    }

    public static CGCanvas Canvas(CGDrawable c1, CGDrawable c2, CGDrawable c3) {
        return new CGCanvas(new CGDrawable[] { c1, c2, c3 });
    }

    public static CGCanvas Canvas(CGDrawable c1, CGDrawable c2, CGDrawable c3, CGDrawable c4) {
        return new CGCanvas(new CGDrawable[] { c1, c2, c3, c4 });
    }

    public static CGCanvas Canvas(CGDrawable c1, CGDrawable c2, CGDrawable c3, CGDrawable c4, CGDrawable c5) {
        return new CGCanvas(new CGDrawable[] { c1, c2, c3, c4, c5 });
    }

    public static CGCanvas Canvas(CGDrawable[] content) {
        return new CGCanvas(content);
    }
}
