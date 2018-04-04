package com.dataprice.ui.products;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ComboBoxWithButton extends CssLayout {

    private final ComboBox comboBox;
    private final Button button;

    public ComboBoxWithButton(String caption, Resource icon, ClickListener listener) {
        setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        setCaption(caption);

        comboBox = new ComboBox();
        comboBox.setWidth(90, Unit.PERCENTAGE); //iba 100

        button = new Button(icon);
        button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        button.addClickListener(listener);

        addComponents(comboBox, button);
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public Button getButton() {
        return button;
    }
}