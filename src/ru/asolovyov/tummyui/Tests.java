/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.midlet.*;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 * @author Администратор
 */
public class Tests extends MIDlet {

    private Display display;
    private UIForm form = new UIForm("TummyUI");

    private StringBinding labelBinding = new StringBinding("Label");
    private StringBinding textBinding = new StringBinding("Text");
    private BoolBinding ifBinding = new BoolBinding(true);
    
    public void startApp() {
        display = Display.getDisplay(this);
        display.setCurrent(form);
        form.append((UIItem) new UIStringItem(labelBinding, textBinding));
        form.append((Item) new UITextField(labelBinding, textBinding));
        form.append(new UIIfItem(
                ifBinding,
                new UIItem[]{
                    new UIStringItem(null, "Котии:\n"),
                    new UIStringItem(null, "Барсик\n"),
                    new UIStringItem(null, "Мурзик\n"),
                    new UIStringItem(null, "Мурка\n")
                },
                new UIItem[]{
                    new UIStringItem(null, "Пёсели:\n"),
                    new UIStringItem(null, "Шарик\n"),
                    new UIStringItem(null, "Полкан\n"),
                    new UIStringItem(null, "Мухтар\n")
                }));

        form.addCommand(new Command("Опа!", Command.ITEM, 1));
        form.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                ifBinding.setBool(!ifBinding.getBool());
            }
        });
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
