package com.dataprice.ui.feed;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.FeedSettings;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Retail;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showallretails.ShowAllRetailsService;
import com.dataprice.utils.StringUtils;
import com.vaadin.annotations.Push;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Push
@UIScope
@org.springframework.stereotype.Component
public class FeedSettingsLayoutFactory {
	
	private Label mainTittle;	
	
	private Label subTittle;

	private List<String> sellers;
	
	private Settings settings;
				
	private ComboBox sellerName;
	
	private CheckBox checkBox;
	
	private Button generateFeed;
	
	//private DateField lastDate;
	
	private FeedSettings feedSettings;
	
	private class FeedSettingsLayout extends VerticalLayout implements Button.ClickListener {

		private CreateFeedListener createFeedListener;
		private Binder<FeedSettings> binder;
		
		public FeedSettingsLayout(CreateFeedListener createFeedListener) {
			this.createFeedListener = createFeedListener;
		}



		public FeedSettingsLayout init() {

			binder = new Binder<>(FeedSettings.class);
			
			feedSettings = new FeedSettings(settings.getLastUpdateInDays()); //1 day last update!!!
			
			checkBox = new CheckBox(StringUtils.FEEDS_ONLY_MATCHED.getString());
		//	lastDate = new DateField("Seleccione la fecha de última actualización:");
		//	lastDate.setWidth("20%");
			
			mainTittle = new Label("<b><font size=\"5\">"+ StringUtils.FEEDS_TITLE.getString() +"</font></b>",ContentMode.HTML);	
			subTittle = new Label("<font size=\"2\">"+ StringUtils.FEEDS_SUBTITLE.getString() +"</font>",ContentMode.HTML);	
			
			sellerName = new ComboBox(StringUtils.FEEDS_SELLER_SELECTED.getString());
			sellerName.setItems(sellers);
			sellerName.setWidth("50%");
			
			generateFeed = new Button(StringUtils.FEEDS_GENERATE_REPORT.getString());
			generateFeed.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			generateFeed.addClickListener(this);
			generateFeed.setWidth("100%");
			
			return this;
		}
		

       public FeedSettingsLayout bind() {
			
			binder.forField(sellerName)
			  .asRequired("retailer is required")
			  .bind("seller");
			
			binder.forField(checkBox)
			  .bind("onlyMatches");
			
			binder.readBean(feedSettings);
			
			return this;
		}


		public FeedSettingsLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
						
			sellers = showAllProductsService.getSellersList();
						
			return this;
		}
	

		
		public Component layout() {
	
			HorizontalLayout h4 = new HorizontalLayout(generateFeed);
			h4.setWidth("20%");
			h4.setMargin(false);
			
			FormLayout f1 = new FormLayout(sellerName,checkBox);
			
			
			VerticalLayout v2 = new VerticalLayout(mainTittle,subTittle,f1,h4);
			v2.setMargin(false);
			v2.setSizeFull();

			return v2;
		}


		@Override
		public void buttonClick(ClickEvent event) {			
			generateFeed();			
		}


		private void generateFeed() {
			try {
				binder.writeBean(feedSettings);
			} catch (ValidationException e) {
				Notification.show("ERROR","Error in feed Settings",Type.ERROR_MESSAGE);
				return;
			}			
			createFeedListener.createFeed(feedSettings);
		}

	
	}
	
		
	@Autowired 
	private ReportsService reportsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	public Component createComponent(CreateFeedListener createFeedListener) {
		return new FeedSettingsLayout(createFeedListener).load().init().bind().layout();
	}





	

}