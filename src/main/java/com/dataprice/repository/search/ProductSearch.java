package com.dataprice.repository.search;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.BooleanFilter;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.NumericUtils;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Product;

@Repository
@Transactional
public class ProductSearch {

	  @PersistenceContext
	  private EntityManager entityManager;
	 
	  
	  public List search(String text,List<String> wantedSellers) {
		    
		    // get the full text entity manager
		    FullTextEntityManager fullTextEntityManager =
		        org.hibernate.search.jpa.Search.
		        getFullTextEntityManager(entityManager);
		    
		    // create the query using Hibernate Search query DSL
		    QueryBuilder queryBuilder = 
		        fullTextEntityManager.getSearchFactory()
		        .buildQueryBuilder().forEntity(Product.class).get();
		    
		    // a very basic query by keywords
		    org.apache.lucene.search.Query query =
		        queryBuilder
		          .keyword()
		          .onFields("name","sku","upc","brand")
		          .matching(text)
		          .createQuery();

		   
		    //Add filter 
		    if (wantedSellers!=null) {
			    org.apache.lucene.search.Query query2 = applyWantedRetailFilter(query,wantedSellers);
			    
			    // wrap Lucene query in an Hibernate Query object
			    org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query2, Product.class);

			    //Add filter
			    //Add projection to return score, as well as the product itself.
			    //jpaQuery.setProjection(FullTextQuery.SCORE, FullTextQuery.THIS);
			    
			    // execute search and return results (sorted by relevance as default)
			    @SuppressWarnings("unchecked")
			    List results = jpaQuery.getResultList();
			    
			    return results;
		    }else {

			    org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Product.class);
		
			    @SuppressWarnings("unchecked")
			    List results = jpaQuery.getResultList();
			    
			    return results;
		    }

		  } 


	private Query applyWantedRetailFilter(Query query, List<String> wantedSellers) {
      /**
		//FinalQuery is needed for merging a query and a filter (new api)!!
		BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();
		finalQuery.add(query, Occur.MUST); //Occur must needed for setting the query	
		Query query1 = new TermQuery(new Term("seller", wantedSellers)); // We need the term query
		BooleanQuery.Builder booleanQueryfil = new BooleanQuery.Builder();
		booleanQueryfil.add(query1, Occur.MUST);  //boolean query + term query, in order to get something like filterByTerm
		Filter filterQuery = new QueryWrapperFilter(booleanQueryfil.build());		
		finalQuery.add(filterQuery, Occur.FILTER);//Occur filter needed for setting the filter
		return finalQuery.build(); //build generates a query!!
		*/		
		
		//FinalQuery is needed for merging a query and a filter (new api)!!
		BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();
		finalQuery.add(query, Occur.MUST); //Occur must needed for setting the query	
		
		BooleanQuery.Builder booleanQueryfil = new BooleanQuery.Builder();
		for (String wantedSeller : wantedSellers) {	
			Query query1 = new TermQuery(new Term("seller", wantedSeller));	
			booleanQueryfil.add(query1, Occur.SHOULD); //If we have one wantedSeller it has to be must!!
		}
		
		//
		Filter filterQuery = new QueryWrapperFilter(booleanQueryfil.build());	

		finalQuery.add(filterQuery, Occur.FILTER);//Occur filter needed for setting the filter
		return finalQuery.build(); //build generates a query!!
	}
	   
}
