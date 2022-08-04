/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGArc extends CGSomeStrokable {
    private IntBinding startAngleBinding;
    private IntBinding endAngleBinding;

    public CGArc startAngle(IntBinding startAngleBinding) {
        this.startAngleBinding = startAngleBinding;
        this.startAngleBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGArc endAngle(IntBinding endAngleBinding) {
        this.endAngleBinding = endAngleBinding;
        this.endAngleBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public void draw(Graphics g) {
        super.draw(g);

        CGFrame frame = getFrame();
        if (frame == null) {
            return;
        }

        g.fillArc(
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                startAngleBinding.getInt(),
                endAngleBinding.getInt()
                );
    }
}