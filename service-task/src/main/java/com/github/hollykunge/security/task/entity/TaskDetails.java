package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;

/**
 * @author fansq
 * @deprecation 测试
 */
@Data
@Document(collection = "TaskDetails")
public class TaskDetails {

    @Id
    /**
     * 任务id
     */
    private String id;
    /**
     *  任务进度
     */
    @Field("taskProgress")
    private String  taskProgress;
    /**
     * 执行人
     */
    @Field("executor")
    private String  executor;
}
