/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGRectangle extends CGSomeStrokable {
    private Obj cornerRadiusBinding;

    public CGRectangle cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(new Obj(cornerRadius));
    }

    public CGRectangle cornerRaduis(Obj cornerRadiusBinding) {
        this.cornerRadiusBinding = cornerRadiusBinding;
        this.cornerRadiusBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    protected CGSize getCornerRadius() {
        if (this.cornerRadiusBinding != null) {
            return (CGSize)this.cornerRadiusBinding.getObject();
        }
        return CGSize.zero();
    }

    public void draw(Graphics g) {
        super.draw(g);

        CGFrame frame = getFrame();
        if (frame == null) {
            return;
        }

        g.fillRoundRect(
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                getCornerRadius().width,
                getCornerRadius().height
                );
    }
}