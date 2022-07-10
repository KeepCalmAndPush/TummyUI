/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.operators.combining.CombineLatest;
import ru.asolovyov.combime.operators.mapping.Map;

/**
 * @author Администратор
 */
public class Tests extends UIMIDlet {

        int su = 0;
        int sta = 0;
        int des = 0;

        final StringBinding suspendText = new StringBinding("Суспендов: " + su + "\n");
        final StringBinding startText = new StringBinding("Стартов: " + sta + "\n");
        final StringBinding destroyText = new StringBinding("Дестроев: " + des + "\n");

    
    private StringBinding labelBinding = Binding.String("Label");
    private StringBinding textBinding = Binding.String("Text");
    private BoolBinding isCat = Binding.Bool(false);
    private BoolBinding visiblity = Binding.Bool(false);
    private ArrayBinding listModel = Binding.Array(new Pair[]{
                new Pair("1", "Первый пункт"),
                new Pair("2", "Второй пункт"),
                new Pair("3", "Третий пункт\n")
            });


    UIItem bigItem = UI.Group(
            UI.StringItem(labelBinding, textBinding),
            UI.TextField(labelBinding, textBinding),//.isVisible(visiblity),
            UI.StringItem("isVisible", "true").isVisible(visiblity),
            UI.StringItem("isVisible", "false").isVisible(visiblity.to(new Map() {
                    public Object mapValue(Object value) {
                        return new Boolean(!((Boolean) value).booleanValue());
                    }
            })),
            UI.List(listModel, new UIList.ItemFactory() {
                public UIItem itemFor(Object v) {
                    return UI.Wrapper(new StringItem(((Pair) v).name, ((Pair) v).value));
                }
            }),//.visible(visiblity),

            UI.If(isCat)
                    .Then(UI.Group(
                        UI.StringItem(null, "Котии:\n"),
                        UI.StringItem(null, "Барсик").isVisible(visiblity),
                        UI.StringItem(null, "Мурзик"),
                        UI.StringItem(null, "Мурка\n")))
                    .Else(UI.Group(
                        UI.StringItem(null, "Пёсели:\n"),
                        UI.StringItem(null, "Шарик").isVisible(visiblity.inverted()),
                        UI.StringItem(null, "Полкан"),
                        UI.StringItem(null, "Мухтар"))
                        )
                        
                        );

    UIItem lcTracker = UI.Group(
            UI.Group(
            UI.StringItem(suspendText).onPause(new PauseHandler() {
                public void handle() {
                    suspendText.setString("Суспендов: " + ++su + "\n");
                }
            }),
            UI.StringItem(startText).onStart(new StartHandler() {
                public void handle(boolean isResume) {
                    startText.setString("Стартов: " + ++sta + " " + isResume + "\n");
                }
            }),
            UI.StringItem(destroyText).onDestroy(new DestroyHandler() {
                public void handle(boolean b) {
                    startText.setString("Дестроев: " + ++des + "\n");
                }
            }))
    );

    protected UIForm form() {
        return UI.Form("TummyUI",
                bigItem,
                UI.StringItem(textBinding, textBinding.to(new CombineLatest(labelBinding)).to(new Map() {
            public Object mapValue(Object value) {
                String text = (String) (((Object[]) value)[0]);
                String label = (String) (((Object[]) value)[1]);
                if (text == null || label == null) {
                    return "";
                }
                return label + "(" + text.length() + ")";
            }
            })),

            UI.ImageItem(null, Binding.String("res/1.png"), ImageItem.LAYOUT_CENTER, null).isVisible(visiblity)
            )
                    .addCommand(UI.Command("CAT", new UICommand.Handler() {
                    public void handle() {
                        S.println("CAT");
                        isCat.setBool(!isCat.getBool());
                    }}))
                .addCommand(UI.Command("VIS", new UICommand.Handler() {
                        public void handle() {
                            S.println("VIS");
                            visiblity.setBool(!visiblity.getBool());
                    }}))//
                    

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
}
