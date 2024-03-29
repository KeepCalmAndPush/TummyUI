/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms.views;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIStringItem extends UIItem {
    private StringItem stringItem = new StringItem("", "");
    
    public UIStringItem(Str labelBinding, Str textBinding) {
        super();

        labelBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                stringItem.setLabel((String)value);
            }
        });

        textBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                stringItem.setText((String)value);
            }
        });
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (!isVisible) { return new Item[]{}; }
        return new Item[] { this.stringItem };
    }
}
