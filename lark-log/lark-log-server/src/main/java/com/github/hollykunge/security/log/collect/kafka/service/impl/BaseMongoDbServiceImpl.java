package com.github.hollykunge.security.log.collect.kafka.service.impl;

import com.github.hollykunge.security.log.collect.kafka.service.BaseMongoDbService;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author: zhhongyu
 * @description: 基础的mongodb操作实现
 * @since: Create in 9:00 2020/3/5
 */
@Slf4j
public class BaseMongoDbServiceImpl implements BaseMongoDbService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void createCollection(String collectionName) throws Exception{
        checkout(collectionName);
        mongoTemplate.createCollection(collectionName);
    }
    @Override
    public String createIndex(String collectionName, String filedName) throws Exception{
        //配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置为唯一
        options.unique(true);
        //创建按filedName升序排的索引
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(filedName), options);
    }
    @Override
    public List<String> getAllIndexes(String collectionName) throws Exception{
        ListIndexesIterable<Document> documents = mongoTemplate.getCollection(collectionName).listIndexes();
        //上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
        List<String> indexes = new ArrayList<>();
        for (org.bson.Document document : documents) {
            document.entrySet().forEach((key) -> {
                //提取出索引的名称
                if (key.getKey().equals("name")) {
                    indexes.add(key.getValue().toString());
                }
            });
        }
        return indexes;
    }

    @Override
    public void insert(Map<String, Object> object, String collectionName) throws Exception {
        checkout(collectionName);
        mongoTemplate.insert(object, collectionName);
    }

    @Override
    public void insertMany(List<Map<String, Object>> documents, String collectionName)throws Exception {
        checkout(collectionName);
        mongoTemplate.insert(documents, collectionName);
    }

    @Override
    public List<Map> findCondition(Map<String, Object> conditions, String collectionName) throws Exception{
        checkout(collectionName);
        if (conditions == null) {
            return mongoTemplate.findAll(Map.class, collectionName);
        }
        Query query = getEquelQuery(conditions);
        return mongoTemplate.find(query, Map.class, collectionName);
    }

    @Override
    public void upsertOrinsert(Map<String, Object> conditions, String collectionName) throws Exception{
        checkout(collectionName);
        // addToSet 表示将数据塞入document的一个数组成员中
        UpdateResult upResult = mongoTemplate.upsert(getEquelQuery(conditions),
                setUpdate(conditions), collectionName);
        log.debug("更新结果为:----------------->>>>>>>>>>>>>>>>>>>>"+upResult.toString());
    }
    @Override
    public void update(Map<String,Object> indexs,Map<String,Object> contents, String collectionName)throws Exception{
        UpdateResult upResult = mongoTemplate.upsert(getEquelQuery(indexs),
                setUpdate(contents), collectionName);
        log.debug("更新结果为:----------------->>>>>>>>>>>>>>>>>>>>"+upResult.toString());
    }
    @Override
    public void updateFirst(Query query, Update update, String tableName) throws Exception{
        mongoTemplate.updateFirst(query, update, tableName);
    }
    @Override
    public void updateFirst(Map<String,Object> indexs,Map<String,Object> contents, String collectionName) throws Exception{
        mongoTemplate.updateFirst(getEquelQuery(indexs), setUpdate(contents), collectionName);
    }
    @Override
    public void delete(Map<String,Object> conditions,String collectionName)throws Exception{
        checkIndex(conditions,collectionName);
        mongoTemplate.remove(getEquelQuery(conditions),collectionName);
    }
}
