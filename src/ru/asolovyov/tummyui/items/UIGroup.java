/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.util.Vector;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIGroup implements UIItem {
    protected UIForm form;
    protected UIItem[] uiItems = {};

    public UIGroup(UIItem[] uiItems) {
        super();
        this.uiItems = uiItems;
    }

    public void setForm(UIForm form) {
        this.form = form;
        for (int i = 0; i < uiItems.length; i++) {
            UIItem item = uiItems[i];
            item.setForm(form);
        }
    }

    public UIItem[] getUIItems() {
        return uiItems;
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        Vector result = new Vector();
        UIItem[] uiItems = this.getUIItems();
        for (int i = 0; i < uiItems.length; i++) {
            UIItem uiItem = uiItems[i];
            Item[] children = uiItem.getPlainItems();
            for (int c = 0; c < children.length; c++) {
                Item child = children[c];
                result.addElement(child);
            }
        }
        Item[] items = new Item[result.size()];
        result.copyInto(items);
        return items;
    }

    public void itemStateChanged(Item item) {}

    private boolean isVisible = true;
    public UIItem setVisible(BoolBinding binding) {
        final UIItem self = this;
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                isVisible = ((Boolean)value).booleanValue();
                if (form == null) { return; }
                form.layoutChanged(self);
            }
        });
        return this;
    }
}
