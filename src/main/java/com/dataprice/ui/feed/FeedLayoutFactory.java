package com.dataprice.ui.feed;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.FeedSettings;
import com.dataprice.ui.reports.ReportSettingsLayoutFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class FeedLayoutFactory extends VerticalLayout implements View,CreateFeedListener{

	@Autowired
	private FeedSettingsLayoutFactory feedSettingsLayoutFactory;
	
	@Autowired
	private ExportFeedLayoutFactory exportFeedLayoutFactory;
	
	
	@PostConstruct
	void init() {
		
		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
	
	}
	
	
	private void addLayout() {
	    Component feedTab = feedSettingsLayoutFactory.createComponent(this);
		addComponent(feedTab);
		setComponentAlignment(feedTab, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	
	
	
	@Override
	public void createFeed(FeedSettings feedSettings) {
		removeAllComponents();
		Component priceMatrixReport = exportFeedLayoutFactory.createComponent(feedSettings);
		addComponent(priceMatrixReport);		
	}

	
}
