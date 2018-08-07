package com.dataprice.ui;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.dataprice.ui.manager.SpringViewChangeManager;
import com.dataprice.ui.products.ProductLayoutFactory;
import com.dataprice.ui.reports.ReportsMainLayout;
import com.dataprice.ui.settings.SettingsLayoutFactory;
import com.dataprice.ui.tasks.AddTaskFactory;
import com.dataprice.ui.tasks.MySingleThreadPoolExecutor;
import com.dataprice.ui.tasks.TaskExportImportLayoutFactory;
import com.dataprice.ui.tasks.TaskLayoutFactory;
import com.dataprice.ui.user.UserLayoutFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Role;
import com.dataprice.ui.dashboard.DashboardLayoutFactory;
import com.dataprice.ui.feed.FeedLayoutFactory;
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

import java.util.Collection;
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
	
//	private Future<?> job;  //before it was equal to null 

	private MySingleThreadPoolExecutor mySingleThreadPoolExecutor;
	
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

		mySingleThreadPoolExecutor = MySingleThreadPoolExecutor.getInstance();
	//	Future<?> job = (Future<?>) UI.getCurrent().getSession().getAttribute("myJob");
	//	if (job==null) {
	//	   job = MySingleThreadPoolExecutor.getInstance().getCurrentJob();
	//	   UI.getCurrent().getSession().setAttribute("myJob", job);
	//	}
		
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

		this.hybridMenu.setLeftMenuMinimal(!this.hybridMenu.isLeftMenuMinimal());
		// Define the TopMenu in this method
		buildTopMenu(this.hybridMenu);

		// Define the LeftMenu in this method
		buildLeftMenu(this.hybridMenu);

		setContent(this.hybridMenu);
		navigationManager.init(this, hybridMenu.getContent());

		navigationManager.navigateToDefaultView();
		
		
	
	}
	
	private boolean hasRole(String role) {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
		  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  boolean hasRole = false;
		  for (GrantedAuthority authority : authorities) {
		     hasRole = authority.getAuthority().equals(role);
		     if (hasRole) {
		      break;
		     }
		  }
		  return hasRole;
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
				.setButtonCaption(SecurityContextHolder.getContext().getAuthentication().getName())
				.setButtonIcon(new ThemeResource("images/profilDummy.jpg"))
				.addButtonStyleName(EMenuStyle.ICON_RIGHT)
				.addButtonStyleName(EMenuStyle.PROFILVIEW)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.build(hybridMenu);

		userAccountMenu.addLabel("Cuenta Demo");
	//	userAccountMenu.addHr();
		/**
		userAccountMenu.addButton("Ayuda").addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//startTasksExecution();
			}
		});
		*/
		userAccountMenu.addHr();
		userAccountMenu.addButton("Cerrar Sesión").addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//SecurityContext context = SecurityContextHolder.getContext();
		        //context.setAuthentication(null);
				SecurityContextHolder.clearContext();
				//UI.getCurrent().getPage().setLocation("/dataprice-0.0.1-SNAPSHOT/login");
				UI.getCurrent().getPage().setLocation("/login");
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
		if(!hasRole(Role.Retailer.name())) {
        //Only show notifications for the admin.
		TopMenuButton notiButton = TopMenuButtonBuilder.get()
				.setIcon(VaadinIcons.BELL_O)
				.setAlignment(Alignment.MIDDLE_RIGHT)
				.build(hybridMenu);

		this.notificationCenter.setNotificationButton(notiButton);
		
	    }
		
		
		TopMenuLabel label = TopMenuLabelBuilder.get()
			//	.setCaption("<b>Data</b> Price")
				.setCaption("")
				.setIcon(new ThemeResource("images/logo-light.png"))
			//	.setIcon(new ThemeResource("images/logofinal.png"))
				.build(hybridMenu);

	//	label.getComponent().addClickListener(e -> {
	//		UI.getCurrent().getNavigator().navigateTo(HomePage.class.getSimpleName());
	//	});

		/**
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

       
		
		TopMenuButtonBuilder.get()
				.setCaption("Home")
				.setIcon(VaadinIcons.HOME)
				.setNavigateTo(HomePage.class)
				.build(hybridMenu);
       */
	}

	private void buildLeftMenu(HybridMenu hybridMenu) {
		
		if(!hasRole(Role.Retailer.name())) {
		//If is admin user
		/**	
		MenuButton homeButton = LeftMenuButtonBuilder.get()
				.withCaption("Dashboard")
				.withIcon(VaadinIcons.DASHBOARD)
				.withNavigateTo(DashboardLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(homeButton);
           */
        
		MenuButton tasksButton = LeftMenuButtonBuilder.get()
				.withCaption("Bots")
				.withIcon(VaadinIcons.BUG)
				.withNavigateTo(TaskLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(tasksButton);
		
		
		MenuButton exportButton = LeftMenuButtonBuilder.get()
				.withCaption("Exportar/Importar")
				.withIcon(VaadinIcons.REFRESH)
				.withNavigateTo(TaskExportImportLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(exportButton);
		
		/**
		MenuSubMenu bots = LeftMenuSubMenuBuilder.get()
				.setCaption("Bots")
				.setIcon(VaadinIcons.BUG)
				.setConfig(hybridMenu.getConfig())
				.build(hybridMenu);

		bots.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Administrar de Bots")
				.withIcon(VaadinIcons.AUTOMATION)
				.withNavigateTo(TaskLayoutFactory.class)
				.build());

		bots.addLeftMenuButton(LeftMenuButtonBuilder.get()
				.withCaption("Exportar/Importar Bots")
				.withIcon(VaadinIcons.REFRESH)
				.withNavigateTo(TaskExportImportLayoutFactory.class)
				.build());
		*/
		
		
		MenuButton productsButton = LeftMenuButtonBuilder.get()
		.withCaption("Emparejar Productos")
		.withIcon(VaadinIcons.CART)
		.withNavigateTo(ProductLayoutFactory.class)
		.build();

       hybridMenu.addLeftMenuButton(productsButton);

       
		MenuButton settingsButton = LeftMenuButtonBuilder.get()
				.withCaption("Ajustes del Sistema")
				.withIcon(VaadinIcons.COGS)
				.withNavigateTo(SettingsLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(settingsButton);
		
		
		MenuButton reportsButton = LeftMenuButtonBuilder.get()
				.withCaption("Reportes")
				.withIcon(VaadinIcons.CHART_GRID)
				.withNavigateTo(ReportsMainLayout.class)
				.build();

		hybridMenu.addLeftMenuButton(reportsButton);
		
		/**
		MenuButton feedsButton = LeftMenuButtonBuilder.get()
				.withCaption("Feeds")
				.withIcon(VaadinIcons.DATABASE)
				.withNavigateTo(FeedLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(feedsButton);
		
		
		MenuButton userButton = LeftMenuButtonBuilder.get()
				.withCaption("Users")
				.withIcon(VaadinIcons.USER)
				.withNavigateTo(UserLayoutFactory.class)
				.build();

		hybridMenu.addLeftMenuButton(userButton);
		*/
		
		}else {
			//If is retailer
			MenuButton homeButton = LeftMenuButtonBuilder.get()
					.withCaption("Dashboard")
					.withIcon(VaadinIcons.DASHBOARD)
					.withNavigateTo(DashboardLayoutFactory.class)
					.build();

			hybridMenu.addLeftMenuButton(homeButton);
			
			MenuButton reportsButton = LeftMenuButtonBuilder.get()
					.withCaption("Reportes")
					.withIcon(VaadinIcons.CHART_GRID)
					.withNavigateTo(ReportsMainLayout.class)
					.build();

			hybridMenu.addLeftMenuButton(reportsButton);
		}
        
	}

	public HybridMenu getHybridMenu() {
		return hybridMenu;
	}
	
	/**
	 * Tasks Execution Section
	 *
	 */
	
	
	public void startTasksExecution(Runnable run) {
		
		Future<?> job = mySingleThreadPoolExecutor.getCurrentJob();
		if (job==null) {
			job = mySingleThreadPoolExecutor.runTask(run);	
		   }
		 
		}
	
	
	public void stopTasksExecution(){
		
		Future<?> job = mySingleThreadPoolExecutor.getCurrentJob();
		
		if (job!=null) {
				job.cancel(true);
		}
    }
	

	public boolean isTaskSetRunning(){
		Future<?> job = mySingleThreadPoolExecutor.getCurrentJob();
		
		if (job!=null) {
				return true;
		}
		
		return false;
    }

	
	//@WebServlet(urlPatterns = "/*")
    //@VaadinServletConfiguration(ui = VaadinHybridMenuUI.class, productionMode = false)
    //public static class Servlet extends VaadinServlet {
    //}

	/**
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
    */
    
}
