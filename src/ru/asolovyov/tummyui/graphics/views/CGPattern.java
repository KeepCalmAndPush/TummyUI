/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGSize;

/**
 *
 * @author Администратор
 */
public abstract class CGPattern extends CGRectangle {
    protected Size tileSize;

    {
        tileSize = new Size(4, 4);
        tileSize.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
    }

    public abstract void drawTile(Graphics g, CGFrame frame);

    public CGSize tileSize() {
        return tileSize.getCGSize();
    }

    public CGPattern tileSize(CGSize tileSize) {
        this.tileSize.setCGSize(tileSize);
        return this;
    }

    public CGPattern tileSize(Size tileSize) {
        tileSize.route(this.tileSize);
        return this;
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        super.drawContent(g, frame);

        int w = tileSize().width;
        int h = tileSize().height;

        int startX = frame.x + contentInset().left;
        int startY = frame.y + contentInset().top;

        int maxHeight = frame.maxY() - contentInset().vertical();
        int maxWidth  = frame.maxX() - contentInset().horizontal();

        for (int y = startY; y < maxHeight; y += h) {
            for (int x = startX; x < maxWidth; x += w) {
                CGFrame tileFrame = new CGFrame(x, y, w, h);
//                int heightExceed = frame.maxY() - maxHeight;
//                if (heightExceed > 0) {
//                    frame.height -= heightExceed;
//                }
//
//                int widthExceed = frame.maxX() - maxWidth;
//                if (widthExceed > 0) {
//                    frame.width -= widthExceed;
//                }

                drawTile(g, tileFrame);
//                S.println("DRAW TILE " + tileFrame);
            }
        }
    }
}
