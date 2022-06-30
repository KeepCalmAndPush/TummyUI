/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.util.Vector;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public abstract class UIList implements UIItem {
    private UIForm form;
    private ArrayBinding dataSource;
    private UIItem[] uiItems = {};

    public UIList(ArrayBinding dataSource) {
        super();
        
        this.dataSource = dataSource;
        this.dataSource.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                Object[] array = (Object[])value;

                UIItem[] newItems = new UIItem[array.length];
                for (int i = 0; i < array.length; i++) {
                    Object object = array[i];
                    newItems[i] = itemFor(object);
                }
                uiItems = newItems;

                if (form == null) { return; }
                form.layoutChanged(UIList.this);
            }
        });
    }

    public abstract UIItem itemFor(Object viewModel);

    public void setForm(UIForm form) {
        this.form = form;
    }

    public UIItem[] getUIItems() {
//        if (!isVisible) {
//            return new UIItem[]{};
//        }
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
    public UIList setVisible(BoolBinding binding) {
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                isVisible = ((Boolean)value).booleanValue();
                if (form == null) { return; }
                form.layoutChanged(UIList.this);
            }
        });
        return this;
    }
}
