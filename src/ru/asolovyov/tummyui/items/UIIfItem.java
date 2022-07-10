/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;


import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public class UIIfItem extends UIItem {
    private boolean condition = false;
    private BoolBinding conditionBinding;
    private UIItem[] ifItems = {};
    private UIItem[] elseItems = {};

    public UIIfItem(BoolBinding condition, UIGroup ifGroup, UIGroup elseGroup) {
        this(condition, ifGroup.uiItems, elseGroup.uiItems);
    }

    public UIIfItem(BoolBinding condition, UIItem[] ifItems, UIItem[] elseItems) {
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
                        onChanged.sendValue(UIIfItem.this);
                    }
                });
            }
        });

        this.conditionBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                UIIfItem.this.condition = conditionBinding.getBool();
                onChanged.sendValue(UIIfItem.this);
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
