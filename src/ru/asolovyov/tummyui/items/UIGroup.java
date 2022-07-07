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
public class UIGroup extends UIBasicItem {
    protected UIItem[] uiItems = {};

    public UIGroup(UIItem[] uiItems) {
        super();
        this.uiItems = uiItems;
        for (int i = 0; i < this.uiItems.length; i++) { (uiItems[i]).setParent(this); }
    }

    public UIItem[] getUIItems() {
        return uiItems;
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        Vector result = new Vector();
        UIItem[] items = this.getUIItems();
        for (int i = 0; i < items.length; i++) {
            UIItem uiItem = items[i];
            Item[] children = uiItem.getPlainItems();
            for (int c = 0; c < children.length; c++) {
                Item child = children[c];
                result.addElement(child);
            }
        }
        Item[] plainItems = new Item[result.size()];
        result.copyInto(plainItems);
        return plainItems;
    }

    public void itemStateChanged(Item item) {}

    private boolean isVisible = true;
    public UIItem setVisible(BoolBinding binding) {
        final UIItem self = this;
        binding.sink(new Sink() {
            protected void onValue(Object value) {
                boolean visible = ((Boolean)value).booleanValue();
                if (form == null) {
                    isVisible = visible;
                    return;
                }
                
                isVisible = visible;
                form.didChangeLayout(self);
            }
        });
        return this;
    }

    private UIItem parent;
    public UIItem getParent() { return parent; }
    public void setParent(UIItem parent) { this.parent = parent; }
}
