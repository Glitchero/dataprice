package com.dataprice.service.searchproduct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.repository.search.ProductSearch;

@Service
public class SearchProductServiceImpl implements SearchProductService{

	// Inject the UserSearch object
	  @Autowired
	  private ProductSearch productSearch;
	  
	  public List search(String q,List <String> wanted) {
		    List searchResults = null;
		    try {
		      searchResults = productSearch.search(q,wanted);
		    }
		    catch (Exception ex) {
		      // here you should handle unexpected errors
		      // ...
		      // throw ex;
		    }
		   
		    return searchResults;
		  }
	  
}
