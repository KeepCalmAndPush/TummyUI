/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CGFrame;

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
        Image iImage = (Image)image.getObject();
        this.frameBinding.setCGFrame(new CGFrame(0, 0, iImage.getWidth(), iImage.getHeight()));
        
        this.image.sink(new Sink() {
            protected void onValue(Object value) {
                CGImage.this.needsRelayout(getCGFrame());
            }
        });
        return this;
    }

    public void draw(Graphics g) {
        super.draw(g);
        CGFrame frame = frameBinding.getCGFrame().copy();
        Image originalImage = (Image)this.image.getObject();
        g.drawImage(originalImage, frame.x, frame.y, 0);
    }

    public Size intrinsicContentSize() {
        Image originalImage = (Image)this.image.getObject();
        return new Size(originalImage.getWidth(), originalImage.getHeight());
    }

    public CGDrawable sizeToFit() {
        Image image = (Image)this.image.getValue();
        if (image != null) {
            this.width(image.getWidth());
            this.height(image.getHeight());
        }
        return this;
    }
}
