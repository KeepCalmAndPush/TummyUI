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
        this.dataSource.sink(new Sink() {
            protected void onValue(Object value) {
                
                Object[] viewModels = (Object[])value;

                UIItem[] newItems = new UIItem[viewModels.length];
                for (int i = 0; i < viewModels.length; i++) {
                    Object viewModel = viewModels[i];
                    newItems[i] = itemFactory.itemFor(viewModel);
                }

                uiItems = newItems;
                for (int i = 0; i < uiItems.length; i++) {
                    UIItem item = uiItems[i];
                    item.setParent(UIList.this);
                    item.onChanged.sink(new Sink() {
                        protected void onValue(Object value) {
                            onChanged.sendValue(UIList.this);
                        }
                    });
                }
               
                if (form != null) {
                    setForm(form);
                }

                onChanged.sendValue(UIList.this);
            }
        });
    }
}
