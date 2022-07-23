/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIGauge extends UIItem {
    private Gauge gauge;
    private IntBinding valueBinding;

    public UIGauge(StringBinding label, boolean isInteractive, IntBinding maxValue, IntBinding value) {
        super();

        String labelString = label == null ? null : label.getString();
        int valueInt = value == null ? 0 : value.getInt();
        int maxValueInt = maxValue == null ? 10 : maxValue.getInt();

        this.gauge = new Gauge(labelString, isInteractive, maxValueInt, valueInt);

        this.valueBinding = value;

        if (label != null) {
            label.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                gauge.setLabel((String)value);
            }
        });
        }

        if (value != null) {
            value.removeDuplicates().sink(new Sink() {
            protected void onValue(Object o) {
                gauge.setValue(((Integer)o).intValue());
            }
        });
        }

        if (maxValue != null) {
            maxValue.removeDuplicates().sink(new Sink() {
            protected void onValue(Object o) {
                gauge.setMaxValue(((Integer)o).intValue());
            }
        });
        }
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (!isVisible) { return new Item[]{}; }
        return new Item[] { this.gauge };
    }

    public void itemStateChanged(Item item) {
        if (item == this.gauge) {
            this.valueBinding.setInt(gauge.getValue());
        }
    }
}
