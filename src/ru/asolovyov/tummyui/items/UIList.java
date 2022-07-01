/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIList extends UIGroup {
    public static abstract class ItemFactory {
        public abstract UIItem itemFor(Object viewModel);
    }
    
    private ArrayBinding dataSource;
    private ItemFactory itemFactory;

    public UIList(ArrayBinding dataSource, ItemFactory factory) {
        super(new UIItem[]{});

        this.itemFactory = factory;
        
        this.dataSource = dataSource;
        this.dataSource.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                Object[] array = (Object[])value;

                UIItem[] newItems = new UIItem[array.length];
                for (int i = 0; i < array.length; i++) {
                    Object object = array[i];
                    newItems[i] = itemFactory.itemFor(object);
                }

                uiItems = newItems;
                setForm(form);

                if (form == null) { return; }
                form.layoutChanged(UIList.this);
            }
        });
    }
}
