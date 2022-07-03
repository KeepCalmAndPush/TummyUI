/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
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

    private StringBinding labelBinding = new StringBinding("Label");
    private StringBinding textBinding = new StringBinding("Text");
    private BoolBinding isCat = new BoolBinding(true);
    private BoolBinding visiblity = new BoolBinding(true);
    private ArrayBinding listModel = new ArrayBinding(new Pair[] {
        new Pair("1", "Первый пункт"),
        new Pair("2", "Второй пункт"),
        new Pair("3", "Третий пункт\n")
    });

    private Display display;
    private UIForm form = UI.Form("TummyUI",
            UI.StringItem(labelBinding, textBinding),
            UI.TextField(labelBinding, textBinding).isVisible(visiblity),
            UI.StringItem("isVisible", "true").setVisible(visiblity),
            UI.StringItem("isVisible", "false").setVisible(visiblity.inverted()),
            UI.List(listModel, new UIList.ItemFactory() {
                public UIItem itemFor(Object v) {
                    return UI.Wrapper(new StringItem(((Pair)v).name, ((Pair)v).value));
                }}).setVisible(visiblity),
            UI.If(isCat)
              .Then(UI.Group(
                        UI.StringItem(null, "Котии:\n"),
                        UI.StringItem(null, "Барсик").setVisible(visiblity.inverted()),
                        UI.StringItem(null, "Мурзик"),
                        UI.StringItem(null, "Мурка\n")))
              .Else(UI.Group(
                        UI.StringItem(null, "Пёсели:\n"),
                        UI.StringItem(null, "Шарик").setVisible(visiblity),
                        UI.StringItem(null, "Полкан"),
                        UI.StringItem(null, "Мухтар")))
          );
    
    public void startApp() {
        display = Display.getDisplay(this);
        display.setCurrent(form);

        form.addCommand(new Command("Опа!", Command.ITEM, 1));
        form.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                isCat.setBool(!isCat.getBool());
                visiblity.setBool(!visiblity.getBool());
            }
        });
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    //UI.Command
    //.onDestroy, .onPause на мидлете
    //Кастомный мидлет с методом Вью который вертает форму
}
