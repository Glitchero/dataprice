package com.dataprice.ui.manager;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import com.dataprice.ui.dashboard.DashboardLayoutFactory;
import com.dataprice.ui.reports.ReportsMainLayout;
import com.dataprice.ui.tasks.TaskLayoutFactory;
import com.dataprice.ui.util.SpringViewUtil;
import com.dataprice.ui.view.HomePage;
import org.springframework.stereotype.Component;

/**
 * Governs view navigation of the app.
 */
@Component
@UIScope
public class NavigationManager extends SpringNavigator {

	/**
	 * Navigate to the given view class.
	 *
	 * @param targetView
	 *            the class of the target view, must be annotated using
	 *            {@link SpringView @SpringView}
	 */
	public void navigateTo(Class<? extends View> targetView) {
		String viewId = SpringViewUtil.getViewId(targetView);
		navigateTo(viewId);
	}

	public void navigateTo(Class<? extends View> targetView, Object parameter) {
		String viewId = SpringViewUtil.getViewId(targetView);
		navigateTo(viewId + "/" + parameter.toString());
	}

	public void navigateTo(String navigationState) {
		// Check if the ViewProvider has a view that fits the requested view
		if (null == this.getViewProvider(navigationState)) {
			// If it does not provide a view, try a view name that respects Springs conventions
			navigationState = Conventions.upperCamelToLowerHyphen(navigationState);
		}
		super.navigateTo(navigationState);
	}

	public void navigateToDefaultView() {
		// If the user wants a specific view, it's in the URL.
		// Otherwise navigate to the default page.
		if (!getState().isEmpty()) {
			return;
		}

		// Change default view here
		navigateTo(TaskLayoutFactory.class);
	}

	/**
	 * Update the parameter of the the current view without firing any
	 * navigation events.
	 *
	 * @param parameter
	 *            the new parameter to set, never <code>null</code>,
	 *            <code>""</code> to not use any parameter
	 */
	public void updateViewParameter(String parameter) {
		String viewName = SpringViewUtil.getViewId(getCurrentView().getClass());
		String parameters;
		if (parameter == null) {
			parameters = "";
		} else {
			parameters = parameter;
		}

		updateNavigationState(new ViewChangeEvent(this, getCurrentView(), getCurrentView(), viewName, parameters));
	}

}
