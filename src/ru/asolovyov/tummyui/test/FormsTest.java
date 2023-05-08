package ru.asolovyov.tummyui.test;

import java.util.Date;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.tummyui.data.ListItem;
import ru.asolovyov.tummyui.forms.UI;
import ru.asolovyov.tummyui.forms.UICommand;
import ru.asolovyov.tummyui.forms.UIEnvironment;
import ru.asolovyov.tummyui.forms.UIMIDlet;
import ru.asolovyov.tummyui.forms.views.UIForEach.ItemFactory;
import ru.asolovyov.tummyui.forms.views.UIItem;

public class FormsTest extends UIMIDlet {

    private Bool alertTrigger = new Bool(false);
    private Bool isOdd = new Bool(true);
    private Arr oddValues = new Arr(new Object[]{"1", "3"});
    private Arr evenValues = new Arr(new Object[]{"2", "4"});

    protected Displayable content() {
        return UI.Form("Forms",
                UI.If(isOdd)
                    .Then(
                        UI.ForEach(oddValues, new ItemFactory() {
                            public UIItem itemFor(Object viewModel) {
                                return UI.StringItem("Odd:", (String) viewModel);
                            }
                    })).Else(
                        UI.ForEach(evenValues, new ItemFactory() {
                            public UIItem itemFor(Object viewModel) {
                                return UI.StringItem("Even:", (String) viewModel);
                            }
                    })),
                UI.DateField("Today is:", DateField.DATE_TIME, new Date()),

                UI.Group(
                    UI.StringItem(UIEnvironment.put("hello-world-key", "Hello, world!")),
                    UI.TextField(UIEnvironment.string("hello-world-key")),
                    UI.Gauge("Gauge", true, 1, 10)),
                    UI.ChoiceGroup("Are you a", ChoiceGroup.EXCLUSIVE, new ListItem[]{
                        new ListItem("Cat person", null, true),
                        new ListItem("Dog person", null, false)
                }),

                UI.ImageItem(null, "res/1.png", 0, "Cat")
               )
               .alert(alertTrigger, UI.Alert("ALERT!", "Hello!", null, AlertType.ALARM))
               .command(new UICommand("Alert", new UICommand.Handler() {
                    public void handle() {
                        alertTrigger.setBool(!alertTrigger.getBoolean());
                    }
                }))
                .command(new UICommand("If", new UICommand.Handler() {
                    public void handle() {
                        isOdd.setBool(!isOdd.getBoolean());
                    }
                }));
    }
}
