package com.dataprice.model.entity;

import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Bits;


public class SellersFilterFactory {
    private String seller;


    public void setLevel(String seller) {
        this.seller = seller;
    }


    @Factory
    public Filter getFilter() {
         Query query = new TermQuery( new Term( "seller", seller ) );
         return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }
}





