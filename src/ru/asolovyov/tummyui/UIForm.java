/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import java.util.Hashtable;
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
    private static class Items {
        List ui = new List();
        Hashtable map = new Hashtable();

        Items() {
            super();
        }

        Items(Item[] items) {
            for (int i = 0; i < items.length; i++) {
                Item item = items[i];
                UIItem uiItem = new UIPlainItemWrapper(item);
                this.ui.addElement(uiItem);
                this.map.put(uiItem, uiItem.getPlainItems());
            }
        }
        
        Items(UIItem[] uiItems) {
            super();
            this.ui = new List(uiItems);
            for (int i = 0; i < uiItems.length; i++) {
                UIItem uiItem = uiItems[i];
                Item[] items = uiItem.getPlainItems();
                map.put(uiItem, items);
            }
        }

        int startingPlainIndexFor(UIItem uiItem) {
            if (!this.map.containsKey(uiItem) || !this.ui.contains(uiItem)) {
                throw new IllegalArgumentException();
            }
            
            int result = 0;
            for (int u = 0; u < this.ui.size(); u++) {
                UIItem item = (UIItem) this.ui.elementAt(u);
                if (item == uiItem) { return result; }
                result += item.getPlainItems().length;
            }

            throw new IllegalStateException();
        }
        
        Item[] plainArray() {
            List plainList = new List();
            for (int u = 0; u < this.ui.size(); u++) {
                UIItem item = (UIItem) this.ui.elementAt(u);
                Item[] plainItems = item.getPlainItems();
                for (int p = 0; p < plainItems.length; p++) {
                    Item plainItem = plainItems[p];
                    plainList.addElement(plainItem);
                }
            }
            Item[] array = new Item[plainList.size()];
            System.arraycopy(plainList.toArray(), 0, array, 0, array.length);
            return array;
        }
    }

    private Items items = new Items();
    private Vector itemStateListeners = new Vector();
    private ItemStateListener externalItemStateListener;
    
    public UIForm(String title) {
       this(title, new Item[] {});
    }

    public UIForm(String title, Item[] items) {
        super(title, items);
        this.items = new Items(items);
        this.setItemsAsStateListeners(items);
        super.setItemStateListener(this);
    }

    public UIForm(String title, UIItem[] items) {
        this(title, (new Items(items)).plainArray());
    }

    public int append(Item item) {
        int result = super.append(item);
        UIItem uiItem = new UIPlainItemWrapper(item);
        this.items.ui.addElement(uiItem);
        this.items.map.put(uiItem, uiItem.getPlainItems());
        this.setItemsAsStateListeners(this.items.plainArray());
        return result;
    }

    public void append(UIItem uiItem) {
        uiItem.setForm(this);
        this.items.ui.addElement(uiItem);
        
        Item[] plainItems = uiItem.getPlainItems();
        this.items.map.put(uiItem, plainItems);
        for (int i = 0; i < plainItems.length; i++) {
            super.append(plainItems[i]);
        }
        this.setItemsAsStateListeners(this.items.plainArray());
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

    public void layoutChanged(UIItem uiItem) {
        Item[] oldItems = (Item[]) this.items.map.get(uiItem);
        Item[] newItems = uiItem.getPlainItems();
        
        int index = this.items.startingPlainIndexFor(uiItem);

        for (int i = index; i < index + oldItems.length; i++) {
            this.delete(index);
        }

         for (int j = newItems.length - 1; j >= 0; j--) {
            Item item = newItems[j];
            this.insert(index, item);
        }

        this.items.map.put(uiItem, newItems);
        this.setItemsAsStateListeners(uiItem.getPlainItems());
    }

    private void setItemsAsStateListeners(Item[] items) {
        Item[] plains = items;
        for (int i = 0; i < plains.length; i++) {
            Item item = plains[i];
            this.itemStateListeners.removeElement(item);
        }

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
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
