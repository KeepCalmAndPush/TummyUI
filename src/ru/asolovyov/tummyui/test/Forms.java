/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.test;

import java.util.Date;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.operators.combining.CombineLatest;
import ru.asolovyov.combime.operators.mapping.Map;
import ru.asolovyov.tummyui.data.ListItem;
import ru.asolovyov.tummyui.forms.UIEnvironment;
import ru.asolovyov.tummyui.forms.UI;
import ru.asolovyov.tummyui.forms.views.UIForEach;
import ru.asolovyov.tummyui.forms.views.UIItem;
import ru.asolovyov.tummyui.forms.views.UIList;
import ru.asolovyov.tummyui.forms.UIMIDlet;

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
    
    final Str suspendText = new Str("Суспендов: " + su + "\n");
    final Str startText = new Str("Стартов: " + sta + "\n");
    final Str destroyText = new Str("Дестроев: " + des + "\n");
    final Bool alertVisible = new Bool(false);
    private Str labelBinding = new Str("Label");
    private Str textBinding = new Str("Text");
    private Bool isCat = new Bool(false);
    private Bool visiblity = new Bool(false);
    private Arr listModel = new Arr(new Pair[]{
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

    private Arr choiceItems = new Arr(new ListItem[] {
            new ListItem("Привет", null, true),
            new ListItem("Как", null, false),
            new ListItem("Дела", null, true)
        });

    private Str textBoxText = new Str("Текст для текст бокса");
    private Obj dateBinding = new Obj(new Date());

    private Int gaugeBinding = new Int(2);

    protected Displayable content() {
        return UI.Form("TummyUI",
                UI.StringItem(UIEnvironment.put("STRING", "Строка из энвайронмента"))
                )

                // Переделать на билдер
                //.navigationCommand(displayable).title(title).linktitle(link)
           .navigationCommand("TEXT BOX", UI.TextBox(new Str("текст бокс"), UIEnvironment.string("STRING")))
           .navigationCommand("Лист", UI.List(new Str("Hello"), UIList.IMPLICIT, this.choiceItems))
           .navigationCommand("Э1", "ЭНВ 1",
                UI.Form("Э111", UI.TextField(UIEnvironment.string("STRING")))
                .navigationCommand(new Str("Э2"), UI.Form("Э222", UI.StringItem(UIEnvironment.string("STRING"))))
           )
           .navigationCommand("Navi", "ЖЦ ТРЕКЕР", lcTracker)
           .navigationCommand("Канвас", UI.Navigatable(canvas))//
                ;

    }

    private UIItem main = UI.Group(
            UI.Gauge(new Str(gaugeBinding.to(new Map() {
            public Object mapValue(Object value) {
                return "" + value;
            }
        })), true, 10, gaugeBinding),
                UI.TextField(textBoxText.to(new CombineLatest(labelBinding)).to(new Map() {
            public Object mapValue(Object value) {
                String text = null;//(String) (((Object[]) value)[0]);
                String label = null;//(String) (((Object[]) value)[1]);
                if (text == null || label == null) {
                    return "";
                }
                return label + "(" + text.length() + ")";
            }
        }), textBoxText),
                UI.DateField(new Str(dateBinding.to(new Map() {

            public Object mapValue(Object value) {
                Date date = (Date) value;
                return date.toString();
            }
        })), DateField.DATE_TIME, dateBinding),
                UI.ChoiceGroup(new Str("Чойс груп"), ChoiceGroup.MULTIPLE, choiceItems),
                UI.ForEach(choiceItems, new UIForEach.ItemFactory() {

            public UIItem itemFor(Object viewModel) {
                ListItem item = (ListItem) viewModel;
                return UI.StringItem(item.getStringPart(), item.isSelected() ? ":)))" : ":(((");
            }
        }),
                UI.ImageItem(null, "res/1.png", ImageItem.LAYOUT_CENTER, null).isVisible(visiblity)
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
