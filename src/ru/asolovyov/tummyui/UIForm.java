/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

/**
 *
 * @author Администратор
 */
public class UIForm extends Form {
    public UIForm(String title) {
        super(title);
    }

    public UIForm(String title, Item[] items) {
        super(title, items);
    }

    public void addItemStateListener(ItemStateListener listener) {
        
    }
}
