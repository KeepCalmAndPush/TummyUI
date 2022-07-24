/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;


import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.data.List;

/**
 *
 * @author Администратор
 */
public class UIIf extends UIItem {
    private boolean condition = false;
    private BoolBinding conditionBinding;
    private UIItem[] ifItems = {};
    private UIItem[] elseItems = {};

    public UIIf(BoolBinding condition, UIGroup ifGroup, UIGroup elseGroup) {
        this(condition, ifGroup.uiItems, elseGroup.uiItems);
    }

    public UIIf(BoolBinding condition, UIItem[] ifItems, UIItem[] elseItems) {
        super();
        this.conditionBinding = condition;
        this.ifItems = ifItems;
        this.elseItems = elseItems;
        this.condition = condition.getBool();

        for (int i = 0; i < this.ifItems.length; i++) { (this.ifItems[i]).setParent(this); }
        for (int i = 0; i < this.elseItems.length; i++) { (this.elseItems[i]).setParent(this); }

        (new List(this.ifItems)).append(this.elseItems).forEach(new List.Enumerator() {
            public void onElement(Object element) {
                ((UIItem)element).onChanged.sink(new Sink() {
                    protected void onValue(Object value) {
                        UIIf.this.needsRelayout |= ((UIItem)value).needsRelayout;
                        onChanged.sendValue(UIIf.this);
                    }
                });
            }
        });

        this.conditionBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                UIIf.this.condition = conditionBinding.getBool();
                onChanged.sendValue(UIIf.this);
            }
        });
    }

    public UIItem[] getUIItems() {
        if (this.condition) {
            return ifItems;
        }
        return elseItems;
    }
}
