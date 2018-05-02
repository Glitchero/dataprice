package com.dataprice.ui.reports.gridutil.cell;

import java.io.Serializable;

/**
 * Listener for CellFilter changes
 *
 * @author Marten Prieß (http://www.rocketbase.io)
 * @version 1.0
 */
public interface CellFilterChangedListener extends Serializable {

	/**
	 * triggered then filter settings have changed
	 * 
	 * @param cellFilter
	 *            to easily access all parameters and current setttings
	 */
	void changedFilter(final GridCellFilter cellFilter);
}