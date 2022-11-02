/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGText extends CGSomeDrawable implements CGFontSupporting {
    private Str text = new Str("");
    private Obj font = new Obj(Font.getDefaultFont());
    private Int anchor = new Int(CG.CENTER);
    private Int textColor = new Int(0x000000);

    public CGText(Str text) {
        super();
        this.text = text;
        this.text.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });

        this.textColor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
    }
    
    public CGText text(Str text) {
        text.route(this.text);
        return this;
    }

    public CGText text(String text) {
        this.text.setString(text);
        return this;
    }

    public CGText anchor(int anchor) {
        return this.anchor(new Int(anchor));
    }
    
    public CGText anchor(Int anchor) {
        anchor.route(this.anchor);
        return this;
    }

    private int getAnchor() {
        return this.anchor.getInt();
    }

    public Str text() {
        return this.text;
    }

    public void draw(Graphics g) {
        super.draw(g);
        CGFrame frame = getCGFrame();
        if (frame == null) {
            return;
        }
        g.setFont(getFont());
        g.setColor(this.textColor.getInt());

        String text = this.text.getString();

        Font font = this.getFont();

        CGSize textSize = CG.sizeOfString(text, font, frame.getCGSize());
        int textX = frame.x;
        int textY = frame.y;

        int anchor = this.getAnchor();
        if (CG.isBitSet(anchor, CG.HCENTER)) {
            textX += (frame.width - textSize.width) / 2;
        }
        if (CG.isBitSet(anchor, CG.VCENTER)) {
            textY += (frame.height - textSize.height) / 2;
        }
        if (CG.isBitSet(anchor, CG.RIGHT)) {
            textX += (frame.width - textSize.width);
        }
        if (CG.isBitSet(anchor, CG.BOTTOM)) {
            textY += (frame.height - textSize.height);
        }

        char[] delimiters = new char[]{ '\n', '-', ' ', '+', '/', '*', '&', ';', '.', ',' };

        int lineStartIndex = 0;
        int previousDelimiterIndex = 0;

        int lineHeight = font.getHeight();
        int mWidth = font.stringWidth("M");

        int height = 0;

        S.println(text);


        throughText: for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
            char currentCharacter = text.charAt(characterIndex);
            for (int j = 0; j < delimiters.length; j++) {
                char delimiter = delimiters[j];
                if (currentCharacter == delimiter || characterIndex == text.length() - 1) {

                }
        }

        // TODO чот говно. И считалка размера тоже.
        // надо просто идти посимвольно и мерить ширину
        // нашли делиметер - запомнили
        // уперлись в конец строки - откатились либо до предыдущего делиметра,
        // если он не равен началу строки, либо просто обрезали строку и перенесли нас ледующую
        // тогда делимитер - это последний символ текущей строки
        throughText: for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
            char currentCharacter = text.charAt(characterIndex);

            /*
             * NEW LINE STARTED AT: 10
FOUND A CHUNK 'the capacityofthisvector,' WIDTH: 200 FAIL
WILL TRACK BACK! PREV DELIM: 9
WILL CUT FORM 10 TO 9
TRACE: <at java.lang.StringIndexOutOfBoundsException>, Exception caught in Display class
java.lang.StringIndexOutOfBoundsException
             */
            for (int j = 0; j < delimiters.length; j++) {
                char delimiter = delimiters[j];
                if (currentCharacter == delimiter || characterIndex == text.length() - 1) {
                    int endIndex = characterIndex;
                    if (currentCharacter == delimiter && currentCharacter != ' ') {
                        endIndex += 1;
                    }

                    String chunk = text.substring(lineStartIndex, endIndex);
                    int chunkWidth = font.stringWidth(chunk);

                    S.println("FOUND A CHUNK '" + chunk + "' WIDTH: " + chunkWidth + (chunkWidth <= textSize.width ? " OK" : " FAIL"));

                    if (chunkWidth > textSize.width) {
                        S.println("WILL TRACK BACK! PREV DELIM: " + previousDelimiterIndex);
                        String lastChunk = "";
                        int lastChunkWidth = 0;

                        if (height + lineHeight >= textSize.height) {
                            for (int k = lineStartIndex; k < characterIndex; k++) {
                                String chunkToEllipsis = text.substring(lineStartIndex, k);
                                S.println("............WILL ELLIPS: " + chunkToEllipsis);
                                chunkWidth = font.stringWidth(chunkToEllipsis);
                                if (chunkWidth > (textSize.width - mWidth)) {
                                    break;
                                }

                                lastChunk = chunkToEllipsis;
                                lastChunkWidth = chunkWidth;
                            }

                            g.drawString(lastChunk, textX, textY + height, 0);
                            g.drawString("…", textX +lastChunkWidth, textY + height, 0);

                            break throughText;
                        }

                        if (lineStartIndex >= previousDelimiterIndex) {
                            for (int k = lineStartIndex; k < characterIndex; k++) {
                                String chunkToEllipsis = text.substring(lineStartIndex, k);
                                S.println("............WILL ELLIPS: " + chunkToEllipsis);
                                chunkWidth = font.stringWidth(chunkToEllipsis);
                                if (chunkWidth > textSize.width) {
                                    


                                    S.println("WILL CUT FORM " + lineStartIndex + " TO " + (k - 1));
                        String toPrint = text.substring(lineStartIndex, k - 1);
                        S.println("WILL PRINT " + toPrint);

                        g.drawString(toPrint, textX, textY + height, 0);

                        height += lineHeight;

                        lineStartIndex = k;

                        //10 14 23 26 31
                        S.println("NEW LINE STARTED AT: " + lineStartIndex);

                                    
                                }
                            }
                            continue throughText;
                        }

                        S.println("WILL CUT FORM " + lineStartIndex + " TO " + previousDelimiterIndex);
                        String toPrint = text.substring(lineStartIndex, previousDelimiterIndex);
                        S.println("WILL PRINT " + toPrint);
                        
                        g.drawString(toPrint, textX, textY + height, 0);

                        height += lineHeight;

                        lineStartIndex = previousDelimiterIndex + 1;

                        //10 14 23 26 31
                        S.println("NEW LINE STARTED AT: " + lineStartIndex);
                        continue throughText;
                    } else {
                        previousDelimiterIndex = characterIndex;
                    }
                }
            }
        }
    }

    public CGFontSupporting font(Font font) {
        return this.font(new Obj(font));
    }

    public CGFontSupporting font(Obj font) {
        font.route(this.font);
        return this;
    }

    private Font getFont() {
        return (Font)this.font.getObject();
    }

    public CGFontSupporting textColor(int textColorHex) {
        this.textColor.setInt(textColorHex);
        return this;
    }

    public CGFontSupporting textColor(Int textColorHex) {
        this.textColor = textColorHex;
        this.textColor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }
}