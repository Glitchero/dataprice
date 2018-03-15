package com.dataprice.service.removeproduct;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public interface RemoveProductService {

	//Later change this for task, now i dont have a connection between tasks and products.
	public void removeAllProductsFromRetailName(String retailName);
	
}
