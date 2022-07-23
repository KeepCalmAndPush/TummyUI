/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import ru.asolovyov.combime.api.ISubscription;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public class UIForEach extends UIGroup {
    public static abstract class ItemFactory {
        public abstract UIItem itemFor(Object viewModel);
    }
    
    private ArrayBinding dataSource;
    private ItemFactory itemFactory;

    private List subscriptions = new List();

    public UIForEach(ArrayBinding dataSource, ItemFactory factory) {
        super(new UIItem[]{});

        this.itemFactory = factory;
        
        this.dataSource = dataSource;
        this.dataSource.sink(new Sink() {
            protected void onValue(Object value) {
                
                Object[] viewModels = (Object[])value;

                subscriptions.forEach(new List.Enumerator() {
                    public void onElement(Object element) {
                        ((ISubscription) element).cancel();
                    }
                });

                UIItem[] newItems = new UIItem[viewModels.length];
                for (int i = 0; i < viewModels.length; i++) {
                    Object viewModel = viewModels[i];
                    newItems[i] = itemFactory.itemFor(viewModel);
                }

                uiItems = newItems;
                
                for (int i = 0; i < uiItems.length; i++) {
                    UIItem item = uiItems[i];
                    item.setParent(UIForEach.this);
                    Object o = item.onChanged.sink(new Sink() {
                        protected void onValue(Object value) {
                            UIForEach.this.needsRelayout |= ((UIItem)value).needsRelayout;
                            onChanged.sendValue(UIForEach.this);
                        }
                    });
                    subscriptions.addElement(o);
                }
                
                UIForEach.this.needsRelayout = true;
                onChanged.sendValue(UIForEach.this);
            }
        });
    }
}
