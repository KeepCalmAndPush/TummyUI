/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

/**
 *
 * @author Администратор
 */
public interface UIItem extends ItemStateListener {
    public void setForm(UIForm form);
    public UIItem[] getUIItems();
    public Item[] getPlainItems();
}
