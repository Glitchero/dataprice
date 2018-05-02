package com.dataprice.service.dashboard;

import java.util.List;

public interface DashboardService {

    public Integer getNumOfProducts();
	
	public Integer getNumOfTasks();
	
	public Integer getNumOfTasksByStatus(String status);

    public List<String> getRetailersUsed();
    
    public Integer getNumOfTasksByRetail(String retailName);
    
    public List<String> getCompetitorsBySku(String mainSeller);
    
    public List<String> getCompetitorsByUpc(String mainSeller); 
    
    public Integer getTotalOfProductsFromCompetitorByUpc(String mainSeller,String competition);
    
    public Integer getTotalOfProductsFromCompetitorBySku(String mainSeller,String competition);
}
