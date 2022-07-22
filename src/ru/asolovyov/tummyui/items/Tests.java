/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.items;

import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.operators.combining.CombineLatest;
import ru.asolovyov.combime.operators.mapping.Map;
import ru.asolovyov.tummyui.models.ListItem;

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
    final Bool alertVisible = Binding.Bool(false);
    private StringBinding labelBinding = Binding.String("Label");
    private StringBinding textBinding = Binding.String("Text");
    private Bool isCat = Binding.Bool(false);
    private Bool visiblity = Binding.Bool(false);
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
            UI.ForEach(listModel, new UIForEach.ItemFactory() {

        public UIItem itemFor(Object v) {
            return UI.Wrapper(new StringItem(((Pair) v).name, ((Pair) v).value));
        }
    }),//.visible(visiblity),

            UI.If(isCat).Then(UI.Group(
            UI.StringItem(null, "Котии:\n"),
            UI.StringItem(null, "Барсик").isVisible(visiblity),
            UI.StringItem(null, "Мурзик"),
            UI.StringItem(null, "Мурка\n"))).Else(UI.Group(
            UI.StringItem(null, "Пёсели:\n"),
            UI.StringItem(null, "Шарик").isVisible(visiblity.inverted()),
            UI.StringItem(null, "Полкан"),
            UI.StringItem(null, "Мухтар"))));
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
    })));

    private ArrayBinding choiceItems = Binding.Array(new ListItem[] {
            new ListItem("Привет", null, true),
            new ListItem("Как", null, false),
            new ListItem("Дела", null, true)
        });

    private StringBinding textBoxText = Binding.String("Текст для текст бокса");

    private ObjectBinding dateBinding = Binding.Object(new Date());

    protected UIForm form() {
        return UI.Form("TummyUI",
                UI.DateField("Дата", DateField.DATE_TIME, dateBinding, TimeZone.getDefault()),
                UI.StringItem(new StringBinding(dateBinding.to(new Map() {
            public Object mapValue(Object value) {
                Date date = (Date)value;
                return date.toString();
            }
        }))),

                UI.ChoiceGroup(Binding.String("Чойс груп"), ChoiceGroup.MULTIPLE, choiceItems),
                UI.ForEach(choiceItems, new UIForEach.ItemFactory() {
                public UIItem itemFor(Object viewModel) {
                    ListItem item = (ListItem) viewModel;
                    return UI.StringItem(item.getStringPart(), item.isSelected() ? ":)))" : ":(((");
                    }
                }),
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
                UI.ImageItem(null, "res/1.png", ImageItem.LAYOUT_CENTER, null).isVisible(visiblity))
                .alert(alertVisible, UI.Alert("Hello", "World", UI.Image("res/1.png"), AlertType.ALARM))
                .command(UI.Command("CAT", new UICommand.Handler() {

            public void handle() {
                S.println("CAT");
                isCat.setBool(!isCat.getBool());
            }
        })).command(UI.Command("VIS", new UICommand.Handler() {

            public void handle() {
                S.println("VIS");
                alertVisible.setBool(!alertVisible.getBool());
            }
        }))

                .navigationLink(Binding.String("TEXT BOX"), (UINavigatable)(new UITextBox(Binding.String("текст бокс"), textBoxText)))
                .navigationLink(Binding.String("Лист"), UI.List(Binding.String("Hello"), UIList.IMPLICIT, this.choiceItems))
                .navigationLink(Binding.String("Navi"), null, Binding.String("ЖЦ ТРЕКЕР"), lcTracker)
                .navigationLink(Binding.String("Канвас"), new Canvas() {
            protected void paint(Graphics g) {
                g.setColor(0x00FFFFFF);
                g.fillRect(0, 0, getWidth(), getHeight() / 3);

                g.setColor(0x000000FF);
                g.fillRect(0, getHeight() / 3, getWidth(), getHeight() / 3);

                g.setColor(0x00FF0000);
                g.fillRect(0, 2 * getHeight() / 3, getWidth(), getHeight() / 3);
            }
        })//
                ;

    }

    private class Pair {
        String name;
        String value;

        public Pair(String  name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
