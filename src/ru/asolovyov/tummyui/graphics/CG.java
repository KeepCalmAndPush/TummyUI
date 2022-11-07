/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.tummyui.graphics.views.CGIf;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;
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

    public final static int VALUE_NOT_SET = Integer.MIN_VALUE;

    public final static int HCENTER = Graphics.HCENTER;
    public final static int VCENTER = Graphics.VCENTER;
    public final static int LEFT = Graphics.LEFT;
    public final static int RIGHT = Graphics.RIGHT;
    public final static int TOP = Graphics.TOP;
    public final static int BOTTOM = Graphics.BOTTOM;

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

    public static class MultilineText {
        public int width, height;
        public boolean shouldAddEllipsis = false;
        public List lines = new List();
    }

    public static MultilineText makeMultilineText(String text, Font font, CGSize constrainedSize) {
        MultilineText instructions = new MultilineText();

        final int maxWidth = constrainedSize.width;
        final int maxHeight = constrainedSize.height;

        char[] delimiters = new char[]{ '\n', '-', ' ', '+', '/', '*', '&', ';', '.', ',' };

        int lineStartIndex = 0;
        int latestDelimiterIndex = 0;

        int lineHeight = font.getHeight();

        S.println(text);

        for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
            char currentCharacter = text.charAt(characterIndex);

            boolean isDelimiter = S.contains(currentCharacter, delimiters);
            if (isDelimiter) {
                latestDelimiterIndex = characterIndex;
            }

            int length = characterIndex - lineStartIndex;
            int currentWidth = font.substringWidth(text, lineStartIndex, length);

            boolean isLastCharacter = characterIndex == text.length() - 1;
            boolean isNewLine = currentCharacter == '\n';

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
                latestDelimiterIndex -= 1;
            }

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
