/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import java.util.Vector;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIIfItem implements UIItem {
    private UIForm form;
    private BoolBinding conditionBinding;
    private UIItem[] ifItems = {};
    private UIItem[] elseItems = {};

    public UIIfItem(BoolBinding condition, UIItem[] ifItems, UIItem[] elseItems) {
        super();
        this.conditionBinding = condition;
        this.ifItems = ifItems;
        this.elseItems = elseItems;

        this.conditionBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                if (form == null) { return; }
                form.layoutChanged(UIIfItem.this);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    public UIItem[] getUIItems() {
        if (conditionBinding.getBool()) {
            return ifItems;
        }
        return elseItems;
    }

    public Item[] getPlainItems() {
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
}
