package com.dataprice.service.dashboard;

import java.util.List;

public interface DashboardService {

    public Integer getNumOfProducts();
	
	public Integer getNumOfTasks();
	
	public Integer getNumOfTasksByStatus(String status);

    public List<String> getRetailersUsed();
    
    public Integer getNumOfTasksByRetail(String retailName);
}
