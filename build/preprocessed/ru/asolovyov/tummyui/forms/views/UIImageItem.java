/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms.views;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.mapping.Map;

/**
 *
 * @author Администратор
 */
public class UIImageItem extends UIItem {
    private ImageItem imageItem;

    public UIImageItem(Str label, Str imageName, Int layout, Str altText) {
        this(label, new Obj(imageName.to(new Map() {
            public Object mapValue(Object value) {
                try {
                    return Image.createImage((String)value);
                } catch (IOException ex) {
                    return null;
                }
            }
        })), layout, altText);
    }

    public UIImageItem(String label, Str imageName, int layout, String altText) {
        this(new Str(label), imageName, new Int(layout), new Str(altText));
    }

    public UIImageItem(final Str label, final Obj image, final Int layout, final Str altText) {
        super();

        this.imageItem = new ImageItem(
                label.getString(),
                (Image)image.getObject(),
                layout.getInt(),
                altText.getString()
                );

        label.sink(new Sink() {
            protected void onValue(Object value) {
                imageItem.setLabel(label.getString());
            }
        });

        image.sink(new Sink() {
            protected void onValue(Object value) {
                imageItem.setImage((Image)value);
            }
        });

        layout.sink(new Sink() {
            protected void onValue(Object value) {
                imageItem.setLayout(layout.getInt());
            }
        });

        altText.sink(new Sink() {
            protected void onValue(Object value) {
                imageItem.setAltText((String) value);
            }
        });
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (this.isVisible) {
            return new Item[]{ this.imageItem };
        }
        
        return new Item[]{};
    }
}
