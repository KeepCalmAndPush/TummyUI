/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIDateField extends UIItem {
    private DateField plainItem;
    private ObjectBinding dateBinding;

    public UIDateField(final StringBinding label, final IntBinding mode, final ObjectBinding date, TimeZone timeZone) {
        super();

        this.plainItem = new DateField(label.getString(), mode.getInt(), timeZone);

        this.dateBinding = date;
        
        label.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                plainItem.setLabel(label.getString());
            }
        });

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
