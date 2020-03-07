package com.github.hollykunge.security.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.hollykunge.security.task.entity.ProjectElement;
import com.github.hollykunge.security.task.service.ElementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 资源service
 */
@Service
public class ElementServiceImpl implements ElementService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * fansq
     * @return 资源列表集合
     * [
     *     {
     *         "id": "5e626738a0cf9718aca888ec",
     *         "crtTime": "2020-03-06 20:14:11",
     *         "crtUser": "68yHX85Z",
     *         "crtName": "范少卿",
     *         "crtHost": "192.168.1.3",
     *         "elementId": "37",
     *         "code": "task",
     *         "menuId": "23",
     *         "method": "",
     *         "description": "任务",
     *         "childElementList": [
     *             {
     *                 "id": "5e623e9343dbdf2888d3aeef",
     *                 "crtTime": "2020-03-06 20:14:11",
     *                 "crtUser": "68yHX85Z",
     *                 "crtName": "范少卿",
     *                 "crtHost": "192.168.1.3",
     *                 "elementId": "1",
     *                 "code": "createTask",
     *                 "menuId": "23",
     *                 "method": "POST",
     *                 "description": "创建任务",
     *                 "childElementList": [
     *                     {}
     *                 ]
     *             },{.....}}
     */
    @Override
    public List<ProjectElement> findAllElemet() {
        //todo 我感觉还是有问题 这样写不好 可以直接使用LookupOperation 一次性出结果的
        Query query = new Query(Criteria.where("parent_id").is("0"));
        List<ProjectElement> projectElements = mongoTemplate.find(query,ProjectElement.class);
        for (ProjectElement projectElement:projectElements){
            for(ProjectElement pe :projectElement.getChildElementList()){
                Query querySelect = new Query(Criteria.where("element_id").is(pe.getElementId()));
                ProjectElement mongoTemplateOne = mongoTemplate.findOne(querySelect,ProjectElement.class);
                BeanUtil.copyProperties(mongoTemplateOne,pe);
            }
        }
//        LookupOperation lookupOperation=LookupOperation.newLookup().
//                from("task_project_element").
//                localField("element_id").
//                foreignField("element_id").
//                as("child_elementList");
//        Aggregation aggregation=Aggregation.newAggregation(lookupOperation);
//        List<ProjectElement> results = mongoTemplate.aggregate(aggregation,"task_project_element",ProjectElement.class).getMappedResults();
       return projectElements;
    }

}
