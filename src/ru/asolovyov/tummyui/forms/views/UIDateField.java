/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms.views;

import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIDateField extends UIItem {
    private DateField plainItem;
    private Obj dateBinding;

    public UIDateField(final Str label, final Int mode, final Obj date, TimeZone timeZone) {
        super();

        String labelString = label == null ? null : label.getString();
        this.plainItem = new DateField(labelString, mode.getInt(), timeZone);

        this.dateBinding = date;

        if (label != null) {
            label.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                plainItem.setLabel(label.getString());
            }
        });
        }
        
        mode.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                plainItem.setInputMode(mode.getInt());
            }
        });

        date.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                Date date = (Date)value;
                if (date != plainItem.getDate()) {
                    plainItem.setDate(date);
                }
            }
        });
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        return new Item[]{ this.plainItem };
    }
    
    public void itemStateChanged(Item item) {
        if (item == plainItem) {
            this.dateBinding.sendValue(this.plainItem.getDate());
        }
    }
}
