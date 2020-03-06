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
public class ProjectFeatures extends BaseEntity{

    /**
     * 版本库id
     */
    @Field("features_id")
    private String featuresId;
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
     * 组织id
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     * 团队id
     * fansq 后续补充字段 20-3-4
     */
    @Field("team_code")
    private String team_code;
    /**
     * 项目id
     */
    @Field("project_code")
    private String  projectCode;
}
