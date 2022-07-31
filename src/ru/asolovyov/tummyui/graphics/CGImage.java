/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGImage extends CGSomeDrawable {
    private ObjectBinding image;

    public CGImage(ObjectBinding image) {
        super();
        this.image = image;
        this.image.sink(new Sink() {
            protected void onValue(Object value) {
                CGImage.this.needsRelayout();
            }
        });
    }

    public ObjectBinding image() {
        return this.image;
    }
}
