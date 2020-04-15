package com.github.hollykunge.security.search.bizmd.service.impl;

import com.github.hollykunge.security.search.bizmd.constants.BizConstants;
import com.github.hollykunge.security.search.bizmd.document.GateLogDocument;
import com.github.hollykunge.security.search.bizmd.service.GateLoginStatisticsService;
import com.github.hollykunge.security.search.service.BaseService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;
import org.elasticsearch.search.aggregations.metrics.valuecount.InternalValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 10:33 2020/3/18
 */
@Service
public class GateLoginStatisticsServiceImpl extends BaseService implements GateLoginStatisticsService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map<String, Long> loginNumStatistics(List<String> orgPathCode) throws Exception {
        return num(orgPathCode);
    }

    @Override
    public Map<String, Long> loginTimesStatistics(List<String> orgPathCode) throws Exception {
        return times(orgPathCode);
    }

    private void setQuery(BoolQueryBuilder boolQueryBuilder ,String pathCode){
        boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("pathCode",pathCode));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("uri",BizConstants.LONGIN_URI));
    }

    /**
     * 按orgcode，整体求和统计
     * @param pathCodes
     * @return
     */
    public Map<String,Long> times(List<String> pathCodes){
        Map<String, Long> emptyMap = getEmptyMap();
        for (String pathCode : pathCodes) {
            BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder();
            setQuery(boolQueryBuilder,pathCode);
            //获取注解，通过注解可以得到 indexName 和 type
            Document document = GateLogDocument.class.getAnnotation(Document.class);
            // 构建查询 ， Aggregation 中 login_num 为聚合的结果，pathCode 为字段名称
            ValueCountAggregationBuilder login_time = AggregationBuilders.count("login_time");
            login_time.field("pid");
            SearchQuery searchQuery = getNativeSearchQuery(boolQueryBuilder,login_time,document);
            // 聚合的结果
            Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
            Map<String, Aggregation> results = aggregations.asMap();
            InternalValueCount stringTerms= (InternalValueCount) results.get("login_time");
            emptyMap.put(pathCode, stringTerms.getValue());
        }
        return emptyMap;
    }

    /**
     * 按人员的pid做去重统计
     * @param pathCodes
     * @return
     */
    public Map<String,Long> num(List<String> pathCodes){
        Map<String, Long> emptyMap = getEmptyMap();
        for (String pathCode : pathCodes) {
            BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder();
            setQuery(boolQueryBuilder,pathCode);
            //获取注解，通过注解可以得到 indexName 和 type
            Document document = GateLogDocument.class.getAnnotation(Document.class);
            // 构建查询 ， Aggregation 中 login_num 为聚合的结果，pathCode 为字段名称
            CardinalityAggregationBuilder login_num = AggregationBuilders.cardinality("login_num");
            login_num.field("pid");
            SearchQuery searchQuery = getNativeSearchQuery(boolQueryBuilder,login_num,document);
            // 聚合的结果
            Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
            Map<String, Aggregation> results = aggregations.asMap();
            InternalCardinality internalCardinality = (InternalCardinality) results.get("login_num");
            emptyMap.put(pathCode,internalCardinality.getValue());
        }
        return emptyMap;
    }
}
