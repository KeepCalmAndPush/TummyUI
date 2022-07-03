/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;


import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 * @author Администратор
 */
public class Tests extends UIMIDlet {

    int i = 0;
    
    protected UIForm form() {
        final StringBinding label = new StringBinding("Суспендов:");
        final StringBinding text = new StringBinding("" + i);
        
        return UI.Form(this, "TummyUI",
                UI.StringItem(label, text)
                .onPause(new PauseHandler() { public void handle() {
                            text.setString("" + ++i);
                        }}))
//            UI.StringItem(labelBinding, textBinding),
//            UI.TextField(labelBinding, textBinding),//.isVisible(visiblity),
//            UI.StringItem("isVisible", "true").visible(visiblity),
//            UI.StringItem("isVisible", "false").visible(visiblity.inverted()),
//            UI.List(listModel, new UIList.ItemFactory() {
//                public UIItem itemFor(Object v) {
//                    return UI.Wrapper(new StringItem(((Pair)v).name, ((Pair)v).value));
//                }}),//.visible(visiblity),
//            UI.If(isCat)
//              .Then(UI.Group(
//                        UI.StringItem(null, "Котии:\n"),
//                        UI.StringItem(null, "Барсик").visible(visiblity),
//                        UI.StringItem(null, "Мурзик"),
//                        UI.StringItem(null, "Мурка\n")))
//              .Else(UI.Group(
//                        UI.StringItem(null, "Пёсели:\n"),
//                        UI.StringItem(null, "Шарик").visible(visiblity.inverted()),
//                        UI.StringItem(null, "Полкан"),
//                        UI.StringItem(null, "Мухтар")))
//          )
//          .command(UI.Command("CAT", new UICommand.Handler() { public void handle() {
//                        isCat.setBool(!isCat.getBool());
//                   }}).visible(visiblity))
//          .command(UI.Command("VIS", new UICommand.Handler() { public void handle() {
//                        visiblity.setBool(!visiblity.getBool());
//                   }}))
          ;
    }

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
    private BoolBinding isCat = new BoolBinding(false);
    private BoolBinding visiblity = new BoolBinding(false);
    private ArrayBinding listModel = new ArrayBinding(new Pair[] {
        new Pair("1", "Первый пункт"),
        new Pair("2", "Второй пункт"),
        new Pair("3", "Третий пункт\n")
    });

}
