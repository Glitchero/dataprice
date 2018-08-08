package com.dataprice.service.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.repository.product.ProductRepository;
import com.dataprice.repository.task.TaskRepository;

@Service
@Transactional(readOnly=true)
public class DashboardServiceImpl implements DashboardService {

	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public Integer getNumOfProducts(String mainSeller) {
		return productRepository.getNumOfProducts(mainSeller);
	}

	@Override
	public Integer getNumOfTasks() {
		return taskRepository.getNumOfTasks();
	}

	@Override
	public Integer getNumOfTasksByStatus(String status) {
		return taskRepository.getNumOfTasksByStatus(status);
	}

	@Override
	public List<String> getRetailersUsed() {
		return taskRepository.getRetailersUsed();
	}

	@Override
	public Integer getNumOfTasksByRetail(String retailName) {
		return taskRepository.getNumOfTasksByRetail(retailName);
	}

	@Override
	public List<String> getCompetitorsBySku(String mainSeller) {
		return productRepository.getCompetitorsBySku(mainSeller);
	}

	@Override
	public List<String> getCompetitorsByUpc(String mainSeller) {
		return productRepository.getCompetitorsByUpc(mainSeller);
	}

	@Override
	public Integer getTotalOfProductsFromCompetitorByUpc(String mainSeller, String competition) {
		return productRepository.getTotalOfProductsFromCompetitorByUpc(mainSeller, competition);
	}

	@Override
	public Integer getTotalOfProductsFromCompetitorBySku(String mainSeller, String competition) {
		return productRepository.getTotalOfProductsFromCompetitorBySku(mainSeller, competition);
	}
	
}
