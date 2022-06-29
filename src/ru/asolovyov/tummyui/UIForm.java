/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import java.util.Vector;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import ru.asolovyov.combime.common.S;

/**
 *
 * @author Администратор
 */
public class UIForm extends Form implements ItemStateListener {
    private Vector items = new Vector();
    private Vector itemStateListeners = new Vector();
    private ItemStateListener externalItemStateListener;
    
    public UIForm(String title) {
        super(title);
        super.setItemStateListener(this);
    }

    public UIForm(String title, Item[] items) {
        super(title, items);
        Vector tmpItems = new Vector();
        for (int i = 0; i < items.length; i++) {
            tmpItems.addElement(items[i]);
        }
        this.setItems(tmpItems);
        super.setItemStateListener(this);
    }

    public int append(Item item) {
        int result = super.append(item);
        this.items.addElement(item);
        this.addItemToItemStateListeners(item);
        return result;
    }

    public void addItemStateListener(ItemStateListener listener) {
        this.itemStateListeners.addElement(listener);
    }

    public void removeItemStateListener(ItemStateListener listener) {
        this.itemStateListeners.removeElement(listener);
    }

    public void setItemStateListener(ItemStateListener listener) {
        if (this.externalItemStateListener != null) {
            this.itemStateListeners.removeElement(this.externalItemStateListener);
        }
        this.externalItemStateListener = listener;
        this.itemStateListeners.removeElement(listener);
        this.itemStateListeners.addElement(listener);
    }

    public void itemStateChanged(Item item) {
        for (int i = 0; i < this.itemStateListeners.size(); i++) {
            ItemStateListener listener = (ItemStateListener)this.itemStateListeners.elementAt(i);
            listener.itemStateChanged(item);
        }
    }

    private void setItems(Vector items) {
        for (int i = 0; i < this.items.size(); i++) {
            this.itemStateListeners.removeElement(this.items.elementAt(i));
        }

        this.items = items;

        for (int i = 0; i < this.items.size(); i++) {
            Item item = (Item) items.elementAt(i);
            this.addItemToItemStateListeners(item);
        }
    }

    private void addItemToItemStateListeners(Item item) {
        try {
            UIItem uiItem = (UIItem) item;
            this.addItemStateListener(uiItem);
            uiItem.setForm(this);
        } catch (Exception e) {
            S.println(e);
        }
    }
}
