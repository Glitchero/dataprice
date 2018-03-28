package com.dataprice.repository.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	 
	  
	  public List search(String text) {
		    
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
		          .onFields("name")
		          .matching(text)
		          .createQuery();

		    // wrap Lucene query in an Hibernate Query object
		    org.hibernate.search.jpa.FullTextQuery jpaQuery =
		        fullTextEntityManager.createFullTextQuery(query, Product.class);
		  
		    // execute search and return results (sorted by relevance as default)
		    @SuppressWarnings("unchecked")
		    List results = jpaQuery.getResultList();
		    
		    return results;
		  } // method search
	  
}
