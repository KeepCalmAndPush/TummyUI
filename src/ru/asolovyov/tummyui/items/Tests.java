/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.*;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 * @author Администратор
 */
public class Tests extends MIDlet {

    private class Pair {
        String name;
        String value;
        
        public Pair(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    private Display display;
    private UIForm form = new UIForm("TummyUI");

    private StringBinding labelBinding = new StringBinding("Label");
    private StringBinding textBinding = new StringBinding("Text");
    private BoolBinding ifBinding = new BoolBinding(true);
    private BoolBinding visibilityBinding = new BoolBinding(true);
    private ArrayBinding listModel = new ArrayBinding(new Pair[] { 
        new Pair("1) ", "Первый пункт"),
        new Pair("#2 ", "Второй пункт"),
        new Pair("№3 ", "Третий пункт\n")
    });
    
    public void startApp() {
        display = Display.getDisplay(this);
        display.setCurrent(form);
        form.append((UIItem) new UIStringItem(labelBinding, textBinding).setVisible(visibilityBinding));
        form.append((UIItem) new UITextField(labelBinding, textBinding).setVisible(visibilityBinding));
        form.append(new UIList(this.listModel) {
            public UIItem itemFor(Object viewModel) {
                Pair pair = (Pair) viewModel;
                return new UIPlainItemWrapper(new StringItem(pair.name, pair.value));
            }
        }.setVisible(visibilityBinding));
        form.append(new UIIfItem(
                ifBinding,
                new UIItem[]{
                    new UIStringItem(null, "Котии:\n"),
                    new UIStringItem(null, "Барсик\n").setVisible(visibilityBinding),
                    new UIStringItem(null, "Мурзик\n"),
                    new UIStringItem(null, "Мурка\n")
                },
                new UIItem[]{
                    new UIStringItem(null, "Пёсели:\n"),
                    new UIStringItem(null, "Шарик\n").setVisible(visibilityBinding),
                    new UIStringItem(null, "Полкан\n"),
                    new UIStringItem(null, "Мухтар\n")
                }));

        form.addCommand(new Command("Опа!", Command.ITEM, 1));
        form.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                ifBinding.setBool(!ifBinding.getBool());
                visibilityBinding.setBool(!visibilityBinding.getBool());
            }
        });
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
