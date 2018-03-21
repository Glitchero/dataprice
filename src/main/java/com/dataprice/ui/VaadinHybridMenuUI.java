package com.dataprice.ui;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.dataprice.ui.manager.SpringViewChangeManager;
import com.dataprice.ui.products.ProductLayoutFactory;
import com.dataprice.ui.reports.ReportsLayoutFactory;
import com.dataprice.ui.settings.SettingsLayoutFactory;
import com.dataprice.ui.tasks.TaskLayoutFactory;

import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.ui.classification.BrandMainLayout;
import com.dataprice.ui.classification.CategoryMainLayout;
import com.dataprice.ui.classification.ClassificationLayoutFactory;
import com.dataprice.ui.classification.GenderMainLayout;
import com.dataprice.ui.classification.SubcategoryMainLayout;
import com.dataprice.ui.login.LoginUI;
import com.dataprice.ui.manager.NavigationManager;
import com.dataprice.ui.view.AddStudent;
import com.dataprice.ui.view.GroupPage;
import com.dataprice.ui.view.HomePage;
import com.dataprice.ui.view.MemberPage;
import com.dataprice.ui.view.Operations;
import com.dataprice.ui.view.RemoveStudent;
import com.dataprice.ui.view.SettingsPage;
import com.dataprice.ui.view.ThemeBuilderPage;
import com.dataprice.utils.StringUtils;

import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.builder.HybridMenuBuilder;
import kaesdingeling.hybridmenu.builder.NotificationBuilder;
import kaesdingeling.hybridmenu.builder.left.LeftMenuButtonBuilder;
import kaesdingeling.hybridmenu.builder.left.LeftMenuSubMenuBuilder;
import kaesdingeling.hybridmenu.builder.top.TopMenuButtonBuilder;
import kaesdingeling.hybridmenu.builder.top.TopMenuLabelBuilder;
import kaesdingeling.hybridmenu.builder.top.TopMenuSubContentBuilder;
import kaesdingeling.hybridmenu.components.NotificationCenter;
import kaesdingeling.hybridmenu.data.DesignItem;
import kaesdingeling.hybridmenu.data.MenuConfig;
import kaesdingeling.hybridmenu.data.enums.EMenuComponents;
import kaesdingeling.hybridmenu.data.enums.EMenuStyle;
import kaesdingeling.hybridmenu.data.enums.ENotificationPriority;
import kaesdingeling.hybridmenu.data.leftmenu.MenuButton;
import kaesdingeling.hybridmenu.data.leftmenu.MenuSubMenu;
import kaesdingeling.hybridmenu.data.top.TopMenuButton;
import kaesdingeling.hybridmenu.data.top.TopMenuLabel;
import kaesdingeling.hybridmenu.data.top.TopMenuSubContent;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@PreserveOnRefresh
@SpringUI(path=VaadinHybridMenuUI.NAME)
@Theme("mytheme")
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
@Title("HybridMenu Spring Boot Test")
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
public class VaadinHybridMenuUI extends UI {

	public static final String NAME = "/ui";
	
	private Future<?> job = null;
	
	private final SpringViewProvider viewProvider;
	private final NavigationManager navigationManager;

	private HybridMenu hybridMenu = null;
	private NotificationCenter notificationCenter = null;

	public VaadinHybridMenuUI(SpringViewProvider viewProvider, NavigationManager navigationManager) {
		this.viewProvider = viewProvider;
		this.navigationManager = navigationManager;
		setNavigator(this.navigationManager);
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		UI.getCurrent().setPollInterval(5000);

		MenuConfig menuConfig = new MenuConfig();
		menuConfig.setDesignItem(DesignItem.getWhiteBlueDesign());

		this.notificationCenter = new NotificationCenter(5000);

		this.hybridMenu = HybridMenuBuilder.get()
				.setContent(new VerticalLayout())
				.setMenuComponent(EMenuComponents.LEFT_WITH_TOP)
				.setConfig(menuConfig)
				.withNotificationCenter(this.notificationCenter)
				.setInitNavigator(false)
				.withViewChangeManager(new SpringViewChangeManager())
				.build();

		// Define the TopMenu in this method
		buildTopMenu(this.hybridMenu);

		// Define the LeftMenu in this method
		buildLeftMenu(this.hybridMenu);

		setContent(this.hybridMenu);
		navigationManager.init(this, hybridMenu.getContent());

		navigationManager.navigateToDefaultView();
		
		
	
	}

	private void buildTopMenu(HybridMenu hybridMenu) {

		/**
		TopMenuButtonBuilder.get()
				.setCaption("Home")
				.setIcon(VaadinIcons.HOME)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.setNavigateTo(HomePage.class)
				.build(hybridMenu);

		TopMenuButtonBuilder.get()
				.setCaption("Member")
				.setIcon(VaadinIcons.USER)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.setHideCaption(false)
				.setNavigateTo(MemberPage.class)
				.build(hybridMenu);

		TopMenuButtonBuilder.get()
				.setCaption("Member")
				.setIcon(VaadinIcons.USER)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.setHideCaption(false)
				.addStyleName(EMenuStyle.ICON_RIGHT)
				.setNavigateTo(MemberPage.class)
				.build(hybridMenu);
      */
		TopMenuSubContent userAccountMenu = TopMenuSubContentBuilder.get()
				.setButtonCaption("Rene Tatua Castillo")
				.setButtonIcon(new ThemeResource("images/profilDummy.jpg"))
				.addButtonStyleName(EMenuStyle.ICON_RIGHT)
				.addButtonStyleName(EMenuStyle.PROFILVIEW)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.build(hybridMenu);

		userAccountMenu.addLabel("Account");
		userAccountMenu.addHr();
		userAccountMenu.addButton("Ayuda").addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//startTasksExecution();
			}
		});
		userAccountMenu.addHr();
		userAccountMenu.addButton("Cerrar Sesión").addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//SecurityContext context = SecurityContextHolder.getContext();
		        //context.setAuthentication(null);
				SecurityContextHolder.clearContext();
				UI.getCurrent().getPage().setLocation("/dataprice-0.0.1-SNAPSHOT/login");
			}
		});
		
		/**
		TopMenuButtonBuilder.get()
				.setCaption("Home")
				.setIcon(VaadinIcons.HOME)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.setToolTip("5")
				.setNavigateTo(HomePage.class)
				.build(hybridMenu);
        */
		TopMenuButton notiButton = TopMenuButtonBuilder.get()
				.setIcon(VaadinIcons.BELL_O)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.build(hybridMenu);

		this.notificationCenter.setNotificationButton(notiButton);

		TopMenuLabel label = TopMenuLabelBuilder.get()
				.setCaption("<b>Data</b> Price")
				.setIcon(new ThemeResource("images/hybridmenu-Logo.png"))
				.build(hybridMenu);

		label.getComponent().addClickListener(e -> {
			UI.getCurrent().getNavigator().navigateTo(HomePage.class.getSimpleName());
		});

		
		TopMenuButton notiButtonLow = TopMenuButtonBuilder.get()
				.setCaption("Add Low notification")
				.setIcon(VaadinIcons.BELL_O)
				.setUseOwnListener(true)
				.build(hybridMenu);

		TopMenuButton notiButtonMedium = TopMenuButtonBuilder.get()
				.setCaption("Add Medium notification")
				.setIcon(VaadinIcons.BELL_O)
				.setUseOwnListener(true)
				.build(hybridMenu);

		TopMenuButton notiButtonHigh = TopMenuButtonBuilder.get()
				.setCaption("Add High notification")
				.setIcon(VaadinIcons.BELL_O)
				.setUseOwnListener(true)
				.build(hybridMenu);

		notiButtonLow.addClickListener(e -> {
			NotificationBuilder.get(this.notificationCenter)
					.withCaption("Test")
					.withDescription("This is a LOW notification")
					.withPriority(ENotificationPriority.LOW)
					.withCloseButton()
					.build();
		});

		notiButtonMedium.addClickListener(e -> {
			NotificationBuilder.get(this.notificationCenter)
					.withCaption("Test")
					.withDescription("This is a MEDIUM notification")
					.build();
		});

		notiButtonHigh.addClickListener(e -> {
			NotificationBuilder.get(this.notificationCenter)
					.withCaption("Test")
					.withDescription("This is a HIGH notification")
					.withPriority(ENotificationPriority.HIGH)
					.withIcon(VaadinIcons.INFO)
					.withCloseButton()
					.build();
		});

       
		/**
		TopMenuButtonBuilder.get()
				.setCaption("Home")
				.setIcon(VaadinIcons.HOME)
				.setNavigateTo(HomePage.class)
				.build(hybridMenu);
       */
	}

	private void buildLeftMenu(HybridMenu hybridMenu) {
		
		MenuButton homeButton = LeftMenuButtonBuilder.get()
				.withCaption("Home")
				.withIcon(VaadinIcons.HOME)
				.withNavigateTo(HomePage.class)
				.build();

		hybridMenu.addLeftMenuButton(homeButton);

        
		/**
		MenuButton settingsButton = LeftMenuButtonBuilder.get()
				.withCaption("Settings")
				.withIcon(VaadinIcons.COGS)
				.withNavigateTo(SettingsPage.class)
				.build();

		hybridMenu.addLeftMenuButton(settingsButton);



		MenuSubMenu memberList = LeftMenuSubMenuBuilder.get()
				.setCaption("Member")
				.setIcon(VaadinIcons.USERS)
				.setConfig(hybridMenu.getConfig())
				.build(hybridMenu);

		memberList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Member")
				.withIcon(VaadinIcons.USER)
				.withNavigateTo(MemberPage.class)
				.build());

		memberList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Group")
				.withIcon(VaadinIcons.USERS)
				.withNavigateTo(GroupPage.class)
				.build());
      
		MenuSubMenu demoSettings = LeftMenuSubMenuBuilder.get()
				.setCaption("Settings")
				.setIcon(VaadinIcons.COGS)
				.setConfig(hybridMenu.getConfig())
				.build(hybridMenu);

		LeftMenuButtonBuilder.get()
				.withCaption("White Theme")
				.withIcon(VaadinIcons.PALETE)
				.withClickListener(e -> hybridMenu.switchTheme(DesignItem.getWhiteDesign()))
				.build(demoSettings);

		LeftMenuButtonBuilder.get()
				.withCaption("White Color Theme")
				.withIcon(VaadinIcons.PALETE)
				.withClickListener(e -> hybridMenu.switchTheme(DesignItem.getWhiteBlueDesign()))
				.build(demoSettings);

		LeftMenuButtonBuilder.get()
				.withCaption("Dark Theme")
				.withIcon(VaadinIcons.PALETE)
				.withClickListener(e -> hybridMenu.switchTheme(DesignItem.getDarkDesign()))
				.build(demoSettings);

		LeftMenuButtonBuilder.get()
				.withCaption("Toggle MinimalView")
				.withIcon(VaadinIcons.PALETE)
				.withClickListener(e -> hybridMenu.setLeftMenuMinimal(!hybridMenu.isLeftMenuMinimal()))
				.build(demoSettings);
				
		*/

		
		MenuButton tasksButton = LeftMenuButtonBuilder.get()
				.withCaption("Tasks")
				.withIcon(VaadinIcons.COGS)
				.withNavigateTo(TaskLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(tasksButton);
		
		/**
		MenuButton productsButton = LeftMenuButtonBuilder.get()
				.withCaption("Products")
				.withIcon(VaadinIcons.COGS)
				.withNavigateTo(ProductLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(productsButton);
		*/
		
		MenuSubMenu productList = LeftMenuSubMenuBuilder.get()
				.setCaption("Products")
				.setIcon(VaadinIcons.USERS)
				.setConfig(hybridMenu.getConfig())
				.build(hybridMenu);

		productList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Main")
				.withIcon(VaadinIcons.USER)
				.withNavigateTo(ProductLayoutFactory.class)
				.build());

		productList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Gender")
				.withIcon(VaadinIcons.USERS)
				.withNavigateTo(GenderMainLayout.class)
				.build());
		
		productList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Category")
				.withIcon(VaadinIcons.USERS)
				.withNavigateTo(CategoryMainLayout.class)
				.build());
		
		
		productList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Subcategory")
				.withIcon(VaadinIcons.USERS)
				.withNavigateTo(SubcategoryMainLayout.class)
				.build());
		
		productList.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Brands")
				.withIcon(VaadinIcons.USERS)
				.withNavigateTo(BrandMainLayout.class)
				.build());
		
		MenuButton settingsButton = LeftMenuButtonBuilder.get()
				.withCaption("Settings")
				.withIcon(VaadinIcons.COGS)
				.withNavigateTo(SettingsLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(settingsButton);
		
		
		MenuButton reportsButton = LeftMenuButtonBuilder.get()
				.withCaption("Reports")
				.withIcon(VaadinIcons.COGS)
				.withNavigateTo(ReportsLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(reportsButton);

	}

	public HybridMenu getHybridMenu() {
		return hybridMenu;
	}
	
	/**
	 * Tasks Execution Section
	 *
	 */
	
	/*
	 * Hard Coded way to solve task execution from a button!
	 */
	public void startTasksExecution(Runnable run) {
		
	   if (job==null) {		   
		   job = ThreadPoolInit.executorService.submit(run);	
		     NotificationBuilder.get(this.notificationCenter)
				.withCaption("Test")
				.withDescription("Tasks have been submitted")
				.withPriority(ENotificationPriority.MEDIUM)
				.withIcon(VaadinIcons.INFO)
				.withCloseButton()
				.build();
	   }
	   if (job.isDone()) {
		   job = ThreadPoolInit.executorService.submit(run);
		     NotificationBuilder.get(this.notificationCenter)
				.withCaption("Test")
				.withDescription("Tasks have been submitted")
				.withPriority(ENotificationPriority.MEDIUM)
				.withIcon(VaadinIcons.INFO)
				.withCloseButton()
				.build();
	   }
	 
	}
	
	public void finishTasksExecution(){
				NotificationBuilder.get(this.notificationCenter)
				.withCaption("Test")
				.withDescription("Tasks completed")
				.withPriority(ENotificationPriority.HIGH)
				.withIcon(VaadinIcons.INFO)
				.withCloseButton()
				.build();	
	}
	
	
	public void stopTasksExecution(){
		if (job!=null) {
		
			if (!job.isDone()) {
				job.cancel(true);
				NotificationBuilder.get(this.notificationCenter)
				.withCaption("Test")
				.withDescription("Tasks stopped")
				.withPriority(ENotificationPriority.HIGH)
				.withIcon(VaadinIcons.INFO)
				.withCloseButton()
				.build();	
			}
		}

    }
	

	public boolean isTaskSetRunning(){
		if (job!=null) {
		
			if (!job.isDone()) {
				return true;
			}
		}
		
		return false;

    }


	//@WebServlet(urlPatterns = "/*")
    //@VaadinServletConfiguration(ui = VaadinHybridMenuUI.class, productionMode = false)
    //public static class Servlet extends VaadinServlet {
    //}

    @WebListener
    public static class ThreadPoolInit implements ServletContextListener {

        static ExecutorService executorService;

        @Override
        public void contextInitialized(ServletContextEvent sce) {
        	System.out.println("Empezando executor");
            executorService = Executors.newSingleThreadExecutor();
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
        	executorService.shutdown();
        }
    }
    
    
}
