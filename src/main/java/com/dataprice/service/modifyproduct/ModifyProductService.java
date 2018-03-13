package com.dataprice.service.modifyproduct;

import com.dataprice.model.entity.Product;

public interface ModifyProductService {

	public void modifyProduct(Product productDAO);
	
	public void updateCategoryFromSubcategory(Integer categoryKey, Integer subcategoryKey);
}
