package com.dataprice.ui.reports;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.vaadin.annotations.Push;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@Push
@UIScope
@org.springframework.stereotype.Component
public class MatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	
	private class MatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public MatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public MatrixReportLayout init() {
			productsTable = new Grid<>(Product.class);
			productsTable.removeAllColumns();
			
			
			return this;
		}

		public Component layout() {
			// TODO Auto-generated method stub
			return new Label("<b><font size=\"5\">" + reportSettings.getEndDate() + "</font></b>",ContentMode.HTML);
		}

		public MatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			if (settings.getKeyType().equals("sku")) {
				//System.out.println("Mi tienda " + settings.getMainSeller());
				//for (String cat : reportSettings.getCategories()) {
				//	System.out.println("MI categorias " + cat);
				//}
				
				java.util.Date startDate = java.sql.Date.valueOf(reportSettings.getStartDate());

				Date date2 = java.sql.Date.valueOf(reportSettings.getEndDate());
			
				products = reportsService.getProductsFromSellMatchSkuCatRange(settings.getMainSeller(), reportSettings.getCategories(),startDate,date2);
			}else {
			//	products = showAllProductsService.getProductsFromSellerNameWithMatchesUpc(settings.getMainSeller());
			}
			System.out.println("TOtal de productos: " + products.size());
			return this;
		}
		
	}

	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired 
	private ReportsService reportsService;
	
	public Component createComponent(ReportSettings reportSettings) {
		return new MatrixReportLayout(reportSettings).load().init().layout();
	}

	
}
