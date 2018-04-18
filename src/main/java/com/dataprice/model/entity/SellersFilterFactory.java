package com.dataprice.model.entity;

import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;

public class SellersFilterFactory {
    private String seller;

    /**
     * injected parameter
     */
    public void setLevel(String seller) {
        this.seller = seller;
    }


    @Factory
    public Filter getFilter() {
         Query query = new TermQuery( new Term( "seller", seller ) );
         return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }
}