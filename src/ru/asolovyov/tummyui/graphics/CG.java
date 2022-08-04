/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.B;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public class CG {
    public static CGArc Arc(int startAngle, int endAngle) {
        return new CGArc()
                .startAngle(B.Int(startAngle))
                .endAngle(B.Int(endAngle));
    }

    public static CGArc Circle() {
        return Arc(0, 360);
    }

    public static CGImage Image(Image image) {
        return new CGImage()
                .image(B.Object(image));
    }

    public static CGLine Line() {
        return new CGLine();
    }

    public static CGRect Rect() {
        return new CGRect();
    }

    public static CGText Text(StringBinding string) {
        return new CGText(string);
    }

    public static CGText Text(String string) {
        return new CGText(B.String(string));
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
