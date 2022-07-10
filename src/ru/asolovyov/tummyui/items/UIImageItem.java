/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.mapping.Map;
import ru.asolovyov.combime.publishers.Publisher;

/**
 *
 * @author Администратор
 */
public class UIImageItem extends UIItem {
    private ImageItem imageItem;

    public UIImageItem(StringBinding label, StringBinding imageName, IntBinding layout, StringBinding altText) {
        this(label, new ObjectBinding(imageName.to(new Map() {
            public Object mapValue(Object value) {
                try {
                    return Image.createImage((String)value);
                } catch (IOException ex) {
                    return null;
                }
            }
        })), layout, altText);
    }

    public UIImageItem(String label, StringBinding imageName, int layout, String altText) {
        this(Binding.String(label), imageName, Binding.Int(layout), Binding.String(altText));
    }

    public UIImageItem(final StringBinding label, final ObjectBinding image, final IntBinding layout, final StringBinding altText) {
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
                imageItem.setAltText((String)value);
            }
        });
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        return new Item[]{ this.imageItem };
    }
}
