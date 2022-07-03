/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;


import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIIfItem extends UIBasicItem {
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

        this.conditionBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                if (form == null) { return; }
                
                UIIfItem.this.condition = ((Boolean)value).booleanValue();
                form.didChangeLayout(UIIfItem.this);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
        for (int i = 0; i < ifItems.length; i++) {
            UIItem item = ifItems[i];
            item.setForm(form);
        }
        for (int i = 0; i < elseItems.length; i++) {
            UIItem item = elseItems[i];
            item.setForm(form);
        }
    }

    public UIItem[] getUIItems() {
        if (this.condition) {
            return ifItems;
        }
        return elseItems;
    }
}
