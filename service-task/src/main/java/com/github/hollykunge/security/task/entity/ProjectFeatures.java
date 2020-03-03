package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecation task_project_features 版本库
 */
@Data
@Document(collection="task_project_features")
public class ProjectFeatures {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     * 编号
     */
    @Field("code")
    private String code;
    /**
     * 版本库名称
     */
    @Field("name")
    private String  name;
    /**
     * 描述
     */
    @Field("description")
    private String  description;
    /**
     * 创建时间
     */
    @Field("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @Field("update_time")
    private Date updateTime;
    /**
     * 组织id
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     * 项目id
     */
    @Field("project_code")
    private String  projectCode;
}
