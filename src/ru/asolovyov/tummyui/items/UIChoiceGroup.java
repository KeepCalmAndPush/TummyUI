/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public class UIChoiceGroup extends UIItem {
    public static final class Item {
        private String stringPart;
        private Image imagePart;
        private boolean selected;

        public Item(String stringPart, Image imagePart, boolean isSelected) {
            this.stringPart = stringPart;
            this.imagePart = imagePart;
            this.selected = isSelected;
        }

        public String getStringPart() {
            return stringPart;
        }

        public Image getImagePart() {
            return imagePart;
        }
        
        public boolean isSelected() {
            return selected;
        }
    }
    
    private ChoiceGroup plainItem;
    private ArrayBinding itemsBinding;
    private boolean isSendingUpdates = false;

    public UIChoiceGroup(
            StringBinding label,
            int choiceType,
            ArrayBinding itemsBinding
            ) {
        
        super();
        this.plainItem = new ChoiceGroup(label.getString(), choiceType);

        label.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                plainItem.setLabel((String)value);
            }
        });

        this.itemsBinding = itemsBinding;

        this.itemsBinding.sink(new Sink() {
            protected void onValue(Object value) {
                if (isSendingUpdates) {
                    return;
                }
                
                List items = new List((Object[])value);
                items.forEach(new List.Enumerator() {
                    int i = 0;
                    public void onElement(Object element) {
                        Item item = (Item) element;
                        if (i >= plainItem.size()) {
                            plainItem.append(item.getStringPart(), item.getImagePart());
                        } else {
                            plainItem.set(i, item.getStringPart(), item.getImagePart());
                        }
                        plainItem.setSelectedIndex(i, item.isSelected());
                        i++;
                    }
                });
            }
        });
    }

    public javax.microedition.lcdui.Item[] getPlainItems() {
        if (this.isVisible) {
            return new javax.microedition.lcdui.Item[] { this.plainItem };
        }
        return new javax.microedition.lcdui.Item[]{};
    }

    public void itemStateChanged(javax.microedition.lcdui.Item item) {
        if (this.plainItem != item) {
            return;
        }
        
        this.sendSelectionChanged();
    }

    private void sendSelectionChanged() {
        boolean flags[] = new boolean[this.plainItem.size()];
        this.plainItem.getSelectedFlags(flags);

        Item[] items = (Item[]) this.itemsBinding.getArray();
        for (int i = 0; i < this.plainItem.size(); i++) {
            items[i].selected = flags[i];
        }

        this.isSendingUpdates = true;
        this.itemsBinding.sendValue(items);
        this.isSendingUpdates = false;
    }
}
