/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGRect extends CGSomeStrokable {
    private ObjectBinding cornerRadiusBinding;

    public CGRect cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(Binding.Object(cornerRadius));
    }

    public CGRect cornerRaduis(ObjectBinding cornerRadiusBinding) {
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
        return new CGSize();
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