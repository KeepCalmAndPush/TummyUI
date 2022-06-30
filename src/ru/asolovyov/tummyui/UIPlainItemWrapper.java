/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;

/**
 *
 * @author Администратор
 */
public class UIPlainItemWrapper implements UIItem {
    private Item item;
    private Form form;

    public UIPlainItemWrapper(Item item) {
        super();
        this.item = item;
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        return new Item[]{this.item};
    }

    public void itemStateChanged(Item item) {}
}
