/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.data.test;

import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.B;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.operators.combining.CombineLatest;
import ru.asolovyov.combime.operators.mapping.Map;
import ru.asolovyov.tummyui.data.ListItem;
import ru.asolovyov.tummyui.forms.Environment;
import ru.asolovyov.tummyui.forms.UI;
import ru.asolovyov.tummyui.forms.UIForEach;
import ru.asolovyov.tummyui.forms.UIForm;
import ru.asolovyov.tummyui.forms.UIItem;
import ru.asolovyov.tummyui.forms.UIList;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.forms.UITextBox;

/**
 * @author Администратор
 */
public class Forms extends UIMIDlet {

    private Canvas canvas = new Canvas() {
        protected void paint(Graphics g) {
            g.setColor(0x00FFFFFF);
            g.fillRect(0, 0, getWidth(), getHeight() / 3);

            g.setColor(0x000000FF);
            g.fillRect(0, getHeight() / 3, getWidth(), getHeight() / 3);

            g.setColor(0x00FF0000);
            g.fillRect(0, 2 * getHeight() / 3, getWidth(), getHeight() / 3);
        }
    };

    int su = 0;
    int sta = 0;
    int des = 0;
    
    final StringBinding suspendText = new StringBinding("Суспендов: " + su + "\n");
    final StringBinding startText = new StringBinding("Стартов: " + sta + "\n");
    final StringBinding destroyText = new StringBinding("Дестроев: " + des + "\n");
    final BoolBinding alertVisible = B.Bool(false);
    private StringBinding labelBinding = B.String("Label");
    private StringBinding textBinding = B.String("Text");
    private BoolBinding isCat = B.Bool(false);
    private BoolBinding visiblity = B.Bool(false);
    private ArrayBinding listModel = B.Array(new Pair[]{
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

    private ArrayBinding choiceItems = B.Array(new ListItem[] {
            new ListItem("Привет", null, true),
            new ListItem("Как", null, false),
            new ListItem("Дела", null, true)
        });

    private StringBinding textBoxText = B.String("Текст для текст бокса");
    private ObjectBinding dateBinding = B.Object(new Date());

    private IntBinding gaugeBinding = B.Int(2);

    protected Displayable content() {
        return UI.Form("TummyUI",
                UI.StringItem(Environment.put("STRING", "Строка из энвайронмента"))
                )

                // Переделать на билдер
                //.navigationCommand(displayable).title(title).linktitle(link)
           .navigationCommand(B.String("TEXT BOX"), (new UITextBox(B.String("текст бокс"), Environment.string("STRING"))))
           .navigationCommand(B.String("Лист"), UI.List(B.String("Hello"), UIList.IMPLICIT, this.choiceItems))
           .navigationCommand(B.String("Э1"), B.String("ЭНВ 1"),
                UI.Form("Э111", UI.TextField(Environment.string("STRING")))
                .navigationCommand(B.String("Э2"), UI.Form("Э222", UI.StringItem(Environment.string("STRING"))))
           )
           .navigationCommand(B.String("Navi"), B.String("ЖЦ ТРЕКЕР"), lcTracker)
           .navigationCommand(B.String("Канвас"), UI.Navigatable(canvas))//
                ;

    }

    private UIItem main = UI.Group(
            UI.Gauge(new StringBinding(gaugeBinding.to(new Map() {
            public Object mapValue(Object value) {
                return "" + value;
            }
        })), true, 10, gaugeBinding),
                UI.TextField(textBoxText.to(new CombineLatest(labelBinding)).to(new Map() {
            public Object mapValue(Object value) {
                String text = (String) (((Object[]) value)[0]);
                String label = (String) (((Object[]) value)[1]);
                if (text == null || label == null) {
                    return "";
                }
                return label + "(" + text.length() + ")";
            }
        }), textBoxText),
                UI.DateField(new StringBinding(dateBinding.to(new Map() {

            public Object mapValue(Object value) {
                Date date = (Date) value;
                return date.toString();
            }
        })), DateField.DATE_TIME, dateBinding),
                UI.ChoiceGroup(B.String("Чойс груп"), ChoiceGroup.MULTIPLE, choiceItems),
                UI.ForEach(choiceItems, new UIForEach.ItemFactory() {

            public UIItem itemFor(Object viewModel) {
                ListItem item = (ListItem) viewModel;
                return UI.StringItem(item.getStringPart(), item.isSelected() ? ":)))" : ":(((");
            }
        }),
                UI.ImageItem(null, "res/1.png", ImageItem.LAYOUT_CENTER, null).isVisible(visiblity)
//                ).alert(alertVisible, UI.Alert("Hello", "World", UI.Image("res/1.png"), AlertType.ALARM))
//                .command(UI.Command("CAT", new UICommand.Handler() {
//
//            public void handle() {
//                S.println("CAT");
//                isCat.setBool(!isCat.getBool());
//            }
//        })).command(UI.Command("VIS", new UICommand.Handler() {
//            public void handle() {
//                S.println("VIS");
//                alertVisible.setBool(!alertVisible.getBool());
//            }
//        })
            );

    private class Pair {
        String name;
        String value;

        public Pair(String  name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
