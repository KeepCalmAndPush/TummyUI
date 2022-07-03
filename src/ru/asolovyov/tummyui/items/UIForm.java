/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.util.Enumeration;
import ru.asolovyov.tummyui.utils.List;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

/**
 *
 * @author Администратор
 */
public class UIForm extends Form implements ItemStateListener {
    private List uiItems = new List();

    private Vector itemStateListeners = new Vector();
    private ItemStateListener externalItemStateListener;
    
    public UIForm(String title) {
       this(title, new Item[] {});
    }

    public UIForm(String title, Item[] items) {
        super(title, items);
        super.setItemStateListener(this);
    }

    public UIForm(String title, UIItem[] items) {
        this(title, new Item[]{});
        for (int i = 0; i < items.length; i++) {
            UIItem uiItem = items[i];
            this.appendUI(uiItem);
        }
    }

    public void appendUI(UIItem uiItem) {
        uiItem.setForm(this);
        this.uiItems.addElement(uiItem);
        Item[] plainItems = uiItem.getPlainItems();
        
        for (int i = 0; i < plainItems.length; i++) {
            super.append(plainItems[i]);
        }
        
        this.addItemStateListener(uiItem);
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

    public void willChangeLayout(UIItem uiItem) {
    }

    public void didChangeLayout(UIItem uiItem) {
        this.deleteAll();
        for (int i = 0; i < this.uiItems.size(); i++) {
            UIItem item = (UIItem) this.uiItems.elementAt(i);
            Item[] plainItems = item.getPlainItems();
            for (int j = 0; j < plainItems.length; j++) {
                Item plainItem = plainItems[j];
                this.append(plainItem);
            }
        }
    }
    
    private void deleteAll() {
        int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            this.delete(i);
        }
    }
}
