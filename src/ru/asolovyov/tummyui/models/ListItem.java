/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.models;

import javax.microedition.lcdui.Image;

/**
 *
 * @author Администратор
 */
public final class ListItem {
    private String stringPart;
    private Image imagePart;
    private boolean selected;

    public ListItem(String stringPart, Image imagePart, boolean isSelected) {
        this.stringPart = stringPart;
        this.imagePart = imagePart;
        this.selected = isSelected;
    }

    public String getStringPart() {
        return stringPart;
    }

    public Image getImagePart() {
        return imagePart;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setStringPart(String stringPart) {
        this.stringPart = stringPart;
    }

    public void setImagePart(Image imagePart) {
        this.imagePart = imagePart;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
