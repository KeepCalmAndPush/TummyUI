/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.items;

import java.util.Vector;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.subjects.PassthroughSubject;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public abstract class UIItem implements ItemStateListener {
    protected UIForm form;
    protected UIItem parent;

    private List uiCommands = new List();
    
    protected UIItem getParent() { return parent; }
    protected void setParent(UIItem parent) { this.parent = parent; }

    final PassthroughSubject onChanged = new PassthroughSubject();
    protected boolean isVisible = true;

    private UIMIDlet.StartHandler startHandler;
    private UIMIDlet.DestroyHandler destroyHandler;
    private UIMIDlet.PauseHandler pauseHandler;

    private boolean maySubscribeOnStart = true;
    private boolean maySubscribeOnPause = true;
    private boolean maySubscribeOnDestroy = true;

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }

        Vector result = new Vector();
        UIItem[] uiItems = this.getUIItems();
        for (int i = 0; i < uiItems.length; i++) {
            UIItem uiItem = uiItems[i];
            Item[] children = uiItem.getPlainItems();
            for (int c = 0; c < children.length; c++) {
                Item child = children[c];
                result.addElement(child);
            }
        }
        Item[] items = new Item[result.size()];
        result.copyInto(items);
        return items;
    }

    public void itemStateChanged(Item item) { }

    public UIItem isVisible(BoolBinding binding) {
        final Object o = this;
        binding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                boolean visible = ((Boolean) value).booleanValue();
                isVisible = visible;
                S.println("UIITEM VIS " + isVisible + " " + o);
                onChanged.sendValue(UIItem.this);
            }
        });
        return this;
    }

    protected UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    protected void setForm(UIForm form) {
        this.form = form;
        UIItem[] uiItems = this.getUIItems();
        for (int i = 0; i < uiItems.length; i++) {
            UIItem item = uiItems[i];
            if (item == this) { continue; }
            item.setForm(form);
        }
        this.subscribeOnStartIfPossible();
        this.subscribeOnPauseIfPossible();
        this.subscribeOnDestroyIfPossible();
    }
    
    public UIItem onPause(UIMIDlet.PauseHandler handler) {
        this.pauseHandler = handler;
        subscribeOnPauseIfPossible();
        return this;
    }

    private void subscribeOnPauseIfPossible() {
        if (this.pauseHandler != null && this.form != null && this.form.getMidlet() != null && this.maySubscribeOnPause) {
            this.maySubscribeOnPause = false;
            this.form.getMidlet().getPauseEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    pauseHandler.handle();
                }
            });
        }
    }

    public UIItem onDestroy(UIMIDlet.DestroyHandler handler) {
        this.destroyHandler = handler;
        this.subscribeOnDestroyIfPossible();
        return this;
    }

    private void subscribeOnDestroyIfPossible() {
        if (this.destroyHandler != null && this.form != null && this.form.getMidlet() != null && this.maySubscribeOnDestroy) {
            this.maySubscribeOnDestroy = false;
            this.form.getMidlet().getDestroyEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    boolean unconditional = ((Boolean)value).booleanValue();
                    destroyHandler.handle(unconditional);
                }
            });
        }
    }

    public UIItem onStart(UIMIDlet.StartHandler handler) {
        this.startHandler = handler;
        this.subscribeOnStartIfPossible();
        return this;
    }

    private void subscribeOnStartIfPossible() {
        if (this.startHandler != null && this.form != null && this.form.getMidlet() != null && this.maySubscribeOnStart) {
            this.maySubscribeOnStart = false;
            this.form.getMidlet().getStartEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    boolean isResume = ((Boolean)value).booleanValue();
                    startHandler.handle(isResume);
                }
            }).connect();
        }
    }

    public UIItem addCommand(UICommand cmd) {
        this.getUICommands().addElement(cmd);
        return this;
    }
    
    public List getUICommands() {
        if (this.isVisible) {
            return uiCommands;
        }
        return new List();
    }
}
