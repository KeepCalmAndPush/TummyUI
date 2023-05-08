/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.forms;

import ru.asolovyov.tummyui.forms.views.UIItem;
import javax.microedition.lcdui.Item;

/**
 *
 * @author Администратор
 */
public class UIPlainItemWrapper extends UIItem {
    private Item item;

    public UIPlainItemWrapper(Item item) {
        super();
        this.item = item;
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        return new Item[]{ this.item };
    }
}
