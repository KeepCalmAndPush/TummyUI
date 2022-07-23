/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import java.util.Vector;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public class UIGroup extends UIItem {
    protected UIItem[] uiItems = {};

    public UIGroup(UIItem[] uiItems) {
        super();
        this.uiItems = uiItems;

        (new List(this.uiItems)).forEach(new List.Enumerator() {
            public void onElement(Object element) {
                UIItem item = ((UIItem)element);
                item.setParent(UIGroup.this);
                item.onChanged.sink(new Sink() {
                    protected void onValue(Object value) {
                        UIGroup.this.needsRelayout |= ((UIItem)value).needsRelayout;
                        onChanged.sendValue(UIGroup.this);
                    }
                });
            }
        });
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
}
