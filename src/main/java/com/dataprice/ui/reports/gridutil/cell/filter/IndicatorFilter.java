package com.dataprice.ui.reports.gridutil.cell.filter;


import com.dataprice.ui.reports.Indicator;
import com.vaadin.server.SerializablePredicate;


public class IndicatorFilter implements SerializablePredicate<Indicator> {


	 final String filterString;

	    public IndicatorFilter(String filterString) {
	        this.filterString = filterString;      
	    }

		@Override
		public boolean test(Indicator t) {
			if (filterString == null || t == null) {
	            return false;
	        }
			if (t.getPosition().getCurrentPosition().equals(filterString)) {
				return true;
			}else {
				return false;
			}
		
		}
}

