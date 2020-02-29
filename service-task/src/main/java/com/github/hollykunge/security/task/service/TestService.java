package com.github.hollykunge.security.task.service;

import com.github.hollykunge.security.task.entity.TaskDetails;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * fansq 测试
 * 可以删除
 */
@Service
public class TestService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<TaskDetails> findDetails(){
        return mongoTemplate.findAll(TaskDetails.class);
    }
}
