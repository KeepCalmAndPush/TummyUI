/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIPlainItemWrapper implements UIItem {
    private Item item;
    private UIForm form;

    public UIPlainItemWrapper(Item item) {
        super();
        this.item = item;
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    public UIItem[] getUIItems() {
//        if (!isVisible) {
//            return new UIItem[]{};
//        }
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        return new Item[]{this.item};
    }

    public void itemStateChanged(Item item) {}

    private boolean isVisible = true;
    public UIPlainItemWrapper setVisible(BoolBinding binding) {
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                isVisible = ((Boolean)value).booleanValue();
                if (form == null) { return; }
                form.layoutChanged(UIPlainItemWrapper.this);
            }
        });
        return this;
    }
}
