/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.models.ListItem;

/**
 *
 * @author Администратор
 */
public class UIList extends List implements CommandListener {
    private ArrayBinding itemsBinding;
    private boolean isSendingUpdates = false;
    private Command backCommand = new Command("Назад", Command.BACK, Integer.MIN_VALUE);

    public UIList(
            StringBinding title,
            int choiceType,
            ArrayBinding itemsBinding) {
        super(title.getString(), choiceType);
        
        this.setCommandListener(this);
        this.addCommand(SELECT_COMMAND);
        this.addCommand(this.backCommand);

        title.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                setTitle((String) value);
            }
        });

        this.itemsBinding = itemsBinding;

        this.itemsBinding.sink(new Sink() {
            protected void onValue(Object value) {
                if (isSendingUpdates) {
                    return;
                }

                Object[] array = (Object[]) value;
                for (int i = 0; i < array.length; i++) {
                    ListItem item = (ListItem) array[i];
                    if (i >= size()) {
                        append(item.getStringPart(), item.getImagePart());
                    } else {
                        set(i, item.getStringPart(), item.getImagePart());
                    }
                    setSelectedIndex(i, item.isSelected());
                }
            }
        });
    }

    public void commandAction(Command c, Displayable d) {
        if (d == this) {
            if (c == UIList.SELECT_COMMAND) {
                this.sendSelectionChanged();
                S.println("SELECT");
            }
            if (c == this.backCommand) {
                S.println("NAZAD");
            }
        }
    }

    private void sendSelectionChanged() {
        boolean flags[] = new boolean[this.size()];
        this.getSelectedFlags(flags);

        ListItem[] items = (ListItem[]) this.itemsBinding.getArray();
        for (int i = 0; i < this.size(); i++) {
            items[i].setSelected(flags[i]);
        }

        this.isSendingUpdates = true;
        this.itemsBinding.sendValue(items);
        this.isSendingUpdates = false;
    }
}
