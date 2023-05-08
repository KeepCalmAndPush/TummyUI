/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.tummyui.graphics.views.CGIf;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;
import ru.asolovyov.tummyui.graphics.views.CGStack.DrawableFactory;
import ru.asolovyov.tummyui.graphics.views.CGText;
import ru.asolovyov.tummyui.graphics.views.CGImage;
import ru.asolovyov.tummyui.graphics.views.CGRectangle;
import ru.asolovyov.tummyui.graphics.views.CGArc;
import ru.asolovyov.tummyui.graphics.views.CGCanvas;
import ru.asolovyov.tummyui.graphics.views.CGStack;
import ru.asolovyov.tummyui.graphics.views.CGLine;
import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.tummyui.data.List;

/**
 *
 * @author Администратор
 */
public class CG {
    public final static int FPS = 30;
    public final static int FRAME_MILLIS = 1000 / FPS;

    public final static int NULL = Integer.MIN_VALUE;

    public final static int HCENTER = Graphics.HCENTER;
    public final static int VCENTER = Graphics.VCENTER;
    public final static int LEFT = Graphics.LEFT;
    public final static int RIGHT = Graphics.RIGHT;
    public final static int TOP = Graphics.TOP;
    public final static int BOTTOM = Graphics.BOTTOM;

    public final static int CENTER = HCENTER | VCENTER;

    public final static int KEY_UP = -1;
    public final static int KEY_DOWN = -2;
    public final static int KEY_LEFT = -3;
    public final static int KEY_RIGHT = -4;
    public final static int KEY_ACTION = -5;

    public final static int KEY_1 = 49;
    public final static int KEY_2 = 50;
    public final static int KEY_3 = 51;

    public final static int KEY_4 = 52;
    public final static int KEY_5 = 53;
    public final static int KEY_6 = 54;

    public final static int KEY_7 = 55;
    public final static int KEY_8 = 56;
    public final static int KEY_9 = 57;

    public final static int KEY_STAR = 42;
    public final static int KEY_0 = 48;
    public final static int KEY_HASH = 35;
    
  
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static boolean isBitSet(int mask, int bit) {
        return (mask & bit) == bit;
    }
    
    public static CGArc Arc(int startAngle, int arcAngle) {
        return new CGArc()
                .startAngle(new Int(startAngle))
                .arcAngle(new Int(arcAngle));
    }

    public static CGArc Circle() {
        return Arc(0, 360);
    }

    public static CGImage Image(Image image) {
        return new CGImage()
                .image(new Obj(image));
    }

    public static CGImage Image(String path) {
        Image image = null;
        try {
            image = Image.createImage(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return Image(image);
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
        return (new CGText().text(string));
    }

    public static CGText Text(String string) {
        return (new CGText().text(string));
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
    
    public static CGStack VStack(CGDrawable[] content) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), new Arr(content));
    }

    public static CGStack VStack(CGDrawable d1) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), new Arr(new CGDrawable[] {d1}));
    }

    public static CGStack VStack(CGDrawable d1, CGDrawable d2) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), new Arr(new CGDrawable[] {d1, d2}));
    }

    public static CGStack VStack(CGDrawable d1, CGDrawable d2, CGDrawable d3) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), new Arr(new CGDrawable[] {d1, d2, d3}));
    }

    public static CGStack VStack(CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), new Arr(new CGDrawable[] {d1, d2, d3, d4}));
    }

    public static CGStack VStack(CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4, CGDrawable d5) {
        return new CGStack(new Int(CGStack.AXIS_VERTICAL), new Arr(new CGDrawable[] {d1, d2, d3, d4, d5}));
    }

    public static CGStack HStack(CGDrawable[] content) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), new Arr(content));
    }

    public static CGStack HStack(CGDrawable d1) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), new Arr(new CGDrawable[] {d1}));
    }

    public static CGStack HStack(CGDrawable d1, CGDrawable d2) {
//        //S.println("KEK CGStack(Int axis, Int alignment, Arr drawables) " + new Arr(new CGDrawable[] {d1, d2}));
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), new Arr(new CGDrawable[] {d1, d2}));
    }

    public static CGStack HStack(CGDrawable d1, CGDrawable d2, CGDrawable d3) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), new Arr(new CGDrawable[] {d1, d2, d3}));
    }

    public static CGStack HStack(CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), new Arr(new CGDrawable[] {d1, d2, d3, d4}));
    }

    public static CGStack HStack(CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4, CGDrawable d5) {
        return new CGStack(new Int(CGStack.AXIS_HORIZONTAL), new Arr(new CGDrawable[] {d1, d2, d3, d4, d5}));
    }

    public static CGStack ZStack(CGDrawable[] content) {
        return new CGStack(new Int(CGStack.AXIS_Z), new Arr(content));
    }

    public static CGStack ZStack(CGDrawable d1) {
        return new CGStack(new Int(CGStack.AXIS_Z), new Arr(new CGDrawable[] {d1}));
    }

    public static CGStack ZStack(CGDrawable d1, CGDrawable d2) {
        return new CGStack(new Int(CGStack.AXIS_Z), new Arr(new CGDrawable[] {d1, d2}));
    }

    public static CGStack ZStack(CGDrawable d1, CGDrawable d2, CGDrawable d3) {
        return new CGStack(new Int(CGStack.AXIS_Z), new Arr(new CGDrawable[] {d1, d2, d3}));
    }

    public static CGStack ZStack(CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4) {
        return new CGStack(new Int(CGStack.AXIS_Z), new Arr(new CGDrawable[] {d1, d2, d3, d4}));
    }

    public static CGStack ZStack(CGDrawable d1, CGDrawable d2, CGDrawable d3, CGDrawable d4, CGDrawable d5) {
        return new CGStack(new Int(CGStack.AXIS_Z), new Arr(new CGDrawable[] {d1, d2, d3, d4, d5}));
    }

    public static CGStack VStack(Object[] object, DrawableFactory drawableFactory) {
        return new CGStack(CGStack.AXIS_VERTICAL, object, drawableFactory);
    }

//    public static CGDrawable Switch() {
//
//    }

    public static class MultilineText {
        public int width, height;
        public boolean shouldAddEllipsis = false;
        public List lines = new List();
    }

    private static char[] delimiters = new char[]{ '\n', '-', ' ', '+', '/', '*', '&', ';', '.', ',' };

    public synchronized static MultilineText makeMultilineText(String text, Font font, CGSize constrainedSize) {
        MultilineText instructions = new MultilineText();

        final int maxWidth = constrainedSize.width;
        final int maxHeight = constrainedSize.height;

        int lineStartIndex = 0;
        int latestDelimiterIndex = 0;

        int lineHeight = font.getHeight();

//        //S.println("makeMultilineText FROM TEXT: " + text + " (" + text.length() + " chrs)" + " LINE H: " + lineHeight + " maxW: " + maxWidth + " maxH: " + maxHeight);

        for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
            char currentCharacter = text.charAt(characterIndex);

            boolean isDelimiter = S.contains(currentCharacter, delimiters);
            if (isDelimiter) {
                latestDelimiterIndex = characterIndex;
            }

            int length = characterIndex - lineStartIndex;
//            //S.println("makeMultilineText ci: " + characterIndex + " start: " + lineStartIndex + " length: " + length);
            int currentWidth = font.substringWidth(text, lineStartIndex, length);

            boolean isLastCharacter = characterIndex == text.length() - 1;
            boolean isNewLine = currentCharacter == '\n';

//            //S.println("makeMultilineText currentWidth: " + currentWidth + " isLastChar " + isLastCharacter + " isNewLine " + isNewLine);

            if (currentWidth <= maxWidth && !isLastCharacter && !isNewLine) {
                continue;
            }

            if (isLastCharacter) {
                latestDelimiterIndex = characterIndex + 1;
            } else if(latestDelimiterIndex <= lineStartIndex) {
                latestDelimiterIndex = characterIndex - 1;
            }

            instructions.shouldAddEllipsis = (instructions.height + 2 * lineHeight > maxHeight) && !isLastCharacter;
            if (instructions.shouldAddEllipsis) {
//                //S.println("makeMultilineText SHOULD ADD ELLIPSIS AT " + characterIndex);
                latestDelimiterIndex = Math.min(characterIndex - 1, text.length() - 1);
            }

//            //S.println("makeMultilineText WILL SUBSCTRING stI " + lineStartIndex + " lDI " + latestDelimiterIndex);

            String substringToDraw = text.substring(lineStartIndex, latestDelimiterIndex);
            instructions.height = Math.min(instructions.height + lineHeight, maxHeight);
            
            if (instructions.shouldAddEllipsis) {
                substringToDraw += "…";
            }

            int substringWidth = font.stringWidth(substringToDraw);
            instructions.width = Math.max(substringWidth, instructions.width);
            instructions.width = Math.min(instructions.width, maxWidth);

            instructions.lines.addElement(substringToDraw);

            if (instructions.shouldAddEllipsis) {
                break;
            }
            
            lineStartIndex = latestDelimiterIndex + 1;
        }

        return instructions;
    }

    public static CGSize stringSize(String text, Font font, CGSize constrainedSize) {
        MultilineText multilineText = CG.makeMultilineText(text, font, constrainedSize);
        return new CGSize(multilineText.width, multilineText.height);
    }
}
