/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;
import ru.asolovyov.tummyui.graphics.CGSize;

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
        this.widthBinding.sendValue(new Int(iImage.getWidth()));
        this.heightBinding.sendValue(new Int(iImage.getHeight()));
        
        this.image.sink(new Sink() {
            protected void onValue(Object value) {
                CGImage.this.relayout(frame());
            }
        });

        this.flexibility(new int[]{ CGDrawable.FLEXIBILITY_LOW, CGDrawable.FLEXIBILITY_LOW });

        return this;
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        Image originalImage = (Image)this.image.getObject();

        CGInsets insets = this.contentInset();
        g.drawImage(
                originalImage,
                frame.x + insets.left,
                frame.y + insets.top,
                0
                );
    }

    public CGSize intrinsicContentSize() {
        Image originalImage = (Image)this.image.getObject();
        return new CGSize(originalImage.getWidth(), originalImage.getHeight());
    }

    public CGDrawable sizeToFit() {
        Image image = (Image)this.image.getValue();
        if (image != null) {
            this.widthBinding.sendValue(new Int(image.getWidth()));
            this.heightBinding.sendValue(new Int(image.getHeight()));
        }
        return this;
    }
}
