package com.github.hollykunge.security.log.collect.kafka.service;

import com.alibaba.excel.util.StringUtils;
import com.github.hollykunge.security.common.exception.BaseException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 8:59 2020/3/5
 */
public interface BaseMongoDbService {
    /**
     * 创建一个集合
     * @param collectionName
     * @throws Exception
     */
    void createCollection(String collectionName) throws Exception;

    /**
     * 功能描述: 创建索引
     * 索引是顺序排列，且唯一的索引
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     * @param filedName      对象中的某个属性名
     * @return:java.lang.String
     */
    String createIndex(String collectionName, String filedName) throws Exception;
    /**
     * 功能描述: 获取当前集合对应的所有索引的名称
     *
     * @param collectionName
     * @return:java.util.List<java.lang.String>
     */
    List<String> getAllIndexes(String collectionName) throws Exception;

    /**
     * 指定collectionname创建jsonObject对象
     * @param object
     * @param collectionName
     * @throws Exception
     */
    void insert(Map<String, Object> object, String collectionName) throws Exception;

    /**
     * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
     * @param documents
     * @param collectionName
     */
    void insertMany(List<Map<String, Object>> documents, String collectionName) throws Exception;

    /**
     * 多条件查询collectionName数据，如果条件为空则查询所有的collection数据
     * @param conditions 条件
     * @param collectionName 集合名称
     * @return
     */
    List<Map> findCondition(Map<String, Object> conditions, String collectionName) throws Exception;

    /**
     * 校验集合是否为空
     * @param collectionName
     */
    default void checkout(String collectionName){
        if(StringUtils.isEmpty(collectionName)){
            throw new BaseException("集合名称不能为空...");
        }
    }
    /**
     * 校验条件是否为空
     * @param conditions
     */
    default void checkout(Map<String, Object> conditions){
        if(conditions == null || conditions.size() == 0){
            throw new BaseException("条件不能为空...");
        }
    }

    /**
     * 获取equel类型的查询条件
     * @param conditions
     * @return
     */
    default Query getEquelQuery(Map<String, Object> conditions){
        checkout(conditions);
        Query query = new Query();
        conditions.forEach((key, value) -> {
            query.addCriteria( new Criteria().andOperator(
                    Criteria.where(key).is(value)
            ));
        });
        return query;
    }

    /**
     * 设置update内容
     * @param conditions
     * @return
     */
    default Update setUpdate(Map<String, Object> conditions){
        Update update = new Update();
        conditions.forEach((key,value) ->{
            update.set(key, value);
        });
        return update;
    }

    /**
     * 按条件进行更新，如果没查到，则进行插入数据
     * @param conditions
     * @param collectionName
     */
    void upsertOrinsert(Map<String, Object> conditions, String collectionName) throws Exception;

    /**
     * 功能描述：按索引修改数据
     * @param conditions 索引集
     * @param contents 更新内容
     * @param collectionName 集合名称
     * @throws Exception
     */
    void update(Map<String, Object> conditions, Map<String, Object> contents, String collectionName)throws Exception;

    /**
     * 指定集合名称，判断条件中是否全为集合的索引
     * @param conditions
     * @param collectionName
     * @return
     * @throws Exception
     */
    default boolean checkIndex(Map<String, Object> conditions, String collectionName) throws Exception {
        List<String> allIndexes = getAllIndexes(collectionName);
        return conditions.entrySet().stream().allMatch(key ->allIndexes.contains(key));
    }

    /**
     * 更新一条数据
     * @param query
     * @param update
     * @param tableName
     * @throws Exception
     */
    void updateFirst(Query query, Update update, String tableName) throws Exception;

    void updateFirst(Map<String, Object> indexs, Map<String, Object> contents, String collectionName) throws Exception;

    void delete(Map<String, Object> conditions, String collectionName)throws Exception;
}
