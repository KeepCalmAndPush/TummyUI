/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGImage extends CGSomeDrawable {
    private Obj image;

    public CGImage() {
        super();
    }

    public CGImage(Obj image) {
        super();
        this.image(image);
    }

    public CGImage image(Obj image) {
        this.image = image;
        this.image.sink(new Sink() {
            protected void onValue(Object value) {
                CGImage.this.needsRelayout(getCGFrame());
            }
        });
        return this;
    }
}
