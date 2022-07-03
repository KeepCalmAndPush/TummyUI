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
public class UIBasicItem implements UIItem {
    protected UIForm form;
    protected UIItem parent;

    public UIItem getParent() { return parent; }
    public void setParent(UIItem parent) { this.parent = parent; }

    protected boolean isVisible = true;

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

    public void itemStateChanged(Item item) { }

    public UIItem setVisible(BoolBinding binding) {
        final UIItem self = this;
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                boolean visible = ((Boolean) value).booleanValue();
                if (form == null) {
                    isVisible = visible;
                    return;
                }
                form.willChangeLayout(self);
                isVisible = visible;
                form.didChangeLayout(self);
            }
        });
        return this;
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public void setForm(UIForm form) {
        this.form = form;
        UIItem[] uiItems = this.getUIItems();
        for (int i = 0; i < uiItems.length; i++) {
            UIItem item = uiItems[i];
            if (item == this) { continue; }
            item.setForm(form);
        }
    }
}
