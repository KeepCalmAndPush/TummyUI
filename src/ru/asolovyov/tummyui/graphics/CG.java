/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;

/**
 *
 * @author Администратор
 */
public class CG {
    public final static int FPS = 30;
    public final static int FRAME_MILLIS = 1000 / FPS;

    public final static int HCENTER = 1;
    public final static int VCENTER = 1 << 1;
    public final static int LEFT = 1 << 2;
    public final static int RIGHT = 1 << 3;
    public final static int TOP = 1 << 4;
    public final static int BOTTOM = 1 << 5;

    public final static int CENTER = HCENTER | VCENTER;

    public static boolean isBitSet(int mask, int bit) {
        return (mask & bit) == bit;
    }
    
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
    
    public static CGStack VStack(Int alignment, CGDrawable[] content) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), alignment, new Arr(content));
    }

    public static CGStack VStack(Int alignment, CGDrawable d1) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), alignment, new Arr(new CGDrawable[] {d1}));
    }

    public static CGStack VStack(Int alignment, CGDrawable d1, CGDrawable d2) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), alignment, new Arr(new CGDrawable[] {d1, d2}));
    }

    public static CGStack VStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), alignment, new Arr(new CGDrawable[] {d1, d2, d3}));
    }

    public static CGStack VStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), alignment, new Arr(new CGDrawable[] {d1, d2, d3, d4}));
    }

    public static CGStack VStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4, CGDrawable d5) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), alignment, new Arr(new CGDrawable[] {d1, d2, d3, d4, d5}));
    }

    public static CGStack HStack(Int alignment, CGDrawable[] content) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), alignment, new Arr(content));
    }

    public static CGStack HStack(Int alignment, CGDrawable d1) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), alignment, new Arr(new CGDrawable[] {d1}));
    }

    public static CGStack HStack(Int alignment, CGDrawable d1, CGDrawable d2) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), alignment, new Arr(new CGDrawable[] {d1, d2}));
    }

    public static CGStack HStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), alignment, new Arr(new CGDrawable[] {d1, d2, d3}));
    }

    public static CGStack HStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), alignment, new Arr(new CGDrawable[] {d1, d2, d3, d4}));
    }

    public static CGStack HStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4, CGDrawable d5) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), alignment, new Arr(new CGDrawable[] {d1, d2, d3, d4, d5}));
    }

    public static CGStack ZStack(Int alignment, CGDrawable[] content) {
        return new CGStack(new Int(CGStack.AXIS_Z), alignment, new Arr(content));
    }

    public static CGStack ZStack(Int alignment, CGDrawable d1) {
        return new CGStack(new Int(CGStack.AXIS_Z), alignment, new Arr(new CGDrawable[] {d1}));
    }

    public static CGStack ZStack(Int alignment, CGDrawable d1, CGDrawable d2) {
        return new CGStack(new Int(CGStack.AXIS_Z), alignment, new Arr(new CGDrawable[] {d1, d2}));
    }

    public static CGStack ZStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3) {
        return new CGStack(new Int(CGStack.AXIS_Z), alignment, new Arr(new CGDrawable[] {d1, d2, d3}));
    }

    public static CGStack ZStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4) {
        return new CGStack(new Int(CGStack.AXIS_Z), alignment, new Arr(new CGDrawable[] {d1, d2, d3, d4}));
    }

    public static CGStack ZStack(Int alignment, CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4, CGDrawable d5) {
        return new CGStack(new Int(CGStack.AXIS_Z), alignment, new Arr(new CGDrawable[] {d1, d2, d3, d4, d5}));
    }

    public static CGSize sizeOfString(String text, Font font, CGSize constrainedSize) {
        int width = 0;
        int height = 0;

        final int maxWidth = constrainedSize.width;
        final int maxHeight = constrainedSize.height;

        int lineStartPosition = 0;
        int previousDelimiterPosition = 0;
        int previousChunkWidth = 0;

        /* the index of the first occurrence of the object argument in this vector at position index */

        final int lineHeight = font.getHeight();

        final char[] delimiters = new char[]{ '\n', '-', ' ', '+', '/', '*', '&', ';', '.', ',' };

        throughText: for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            for (int j = 0; j < delimiters.length; j++) {
                char d = delimiters[j];
                if (c == d || i == text.length() - 1) {
                    int endIndex = i;
                    if (c == d && c != ' ') {
                        endIndex += 1;
                    }
                    String chunk = text.substring(lineStartPosition, endIndex);

                    int chunkWidth = font.stringWidth(chunk);

                    if (chunkWidth > maxWidth) {
                        lineStartPosition = previousDelimiterPosition + 1;
                        previousDelimiterPosition = i;

                        if (height + lineHeight > maxHeight) {
                            break throughText;
                        }

                        width = Math.max(chunkWidth, width);
                        width = Math.min(width, maxWidth);

                        height += lineHeight;
                        continue throughText;
                    } else {
                        previousDelimiterPosition = i;
                    }
                }
            }
        }

        height = Math.min(height, maxHeight);

        return new CGSize(width, height);
    }
}
