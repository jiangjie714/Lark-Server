package com.github.hollykunge.security.search.service;

import com.github.hollykunge.security.common.util.Query;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础service类
 */
public class BaseService {
    protected Map<String,Long> getEmptyMap(){
        return new HashMap<>();
    }

    protected void setPageLikeCondition(Query query, BoolQueryBuilder boolQueryBuilder){
        if(query.entrySet().size()>0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.fuzzyQuery(entry.getKey(), entry.getValue().toString()));
            }
        }
    }

    protected BoolQueryBuilder getBoolQueryBuilder(){
        return QueryBuilders.boolQuery();
    }

    protected SearchQuery getNativeSearchQuery(QueryBuilder queryBuilder,
                                               AbstractAggregationBuilder aggregationBuilder,
                                               Document document){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices(document.indexName())
                .withTypes(document.type())
                .addAggregation(aggregationBuilder)
                .build();
        return searchQuery;
    }


}
