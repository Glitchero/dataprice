package com.dataprice.ui.products;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class TextFieldWithTwoButtons extends CssLayout{

	 private final TextField textfield;
	 private final Button button1;
	 private final Button button2;
	 
	 public TextFieldWithTwoButtons(Resource icon1,Resource icon2, ClickListener listener1,ClickListener listener2) {
		    
		    setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		    textfield = new TextField();
		    textfield.setWidth("90%");
		    
		    button1 = new Button(icon1);
	        button1.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
	        button1.addClickListener(listener1);
	        
	        button2 = new Button(icon2);
	        button2.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
	        button2.addClickListener(listener2);
	        
	        addComponents(textfield, button1,button2);
	        
	 }

	public TextField getTextfield() {
		return textfield;
	}

	public Button getButton1() {
		return button1;
	}

	public Button getButton2() {
		return button2;
	}
	 
	 
}
