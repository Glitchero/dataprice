package com.dataprice.ui.reports;

import com.dataprice.model.entity.ReportSettings;

public interface GenerateReportListener {

	public void generateReport(String reportType, ReportSettings reportSettings);
}
