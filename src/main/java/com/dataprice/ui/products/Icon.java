package com.dataprice.ui.products;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class Icon extends Label {
    private static final String CONTENT = "<span class=\"v-icon v-icon-%s\" style=\"font-family: %s; color: %s; font-size: %spx;\">&#x%s;</span>";
    private Color color;
    private int size;
    private VaadinIcons icon;

    public Icon(VaadinIcons icon) {
        this(icon, Color.GREEN, 16);
    }

    public Icon(VaadinIcons icon, Color color, int size) {
        this.icon = icon;
        this.color = color;
        this.size = size;
        setContentMode(ContentMode.HTML);
        updateIcon();
    }

    public void setIcon(VaadinIcons icon) {
        this.icon = icon;
        updateIcon();
    }

    public void setSize(int size) {
        this.size = size;
        updateIcon();
    }

    private void updateIcon() {
        setValue(String.format(CONTENT,
                icon.name(),
                icon.getFontFamily(),
                color.name(),
                size,
                Integer.toHexString(icon.getCodepoint())));
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setRedColor() {
        this.color = Color.RED;
        updateIcon();
    }
    
    public void setYellowColor() {
        this.color = Color.YELLOW;
        updateIcon();
    }
    
    public void setBlueColor() {
        this.color = Color.BLUE;
        updateIcon();
    }

    public enum Color {
        BLACK, GREEN , GRAY, RED, YELLOW, BLUE
    }
}