package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecation task_project_config 项目配置
 */
@Data
@Document(collection="task_project_config")
public class ProjectConfig {
    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;

    @Field("code")
    private String code;

    @Field("project_code")
    private String projectCode;
}
