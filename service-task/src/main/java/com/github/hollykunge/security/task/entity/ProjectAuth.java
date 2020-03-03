package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecation task_project_auth 项目权限
 */
@Data
@Document(collection="task_project_auth")
public class ProjectAuth {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     *权限名称
     */
    @Field("title")
    private String title;
    /**
     *状态 1禁用 0启用
     */
    @Field("status")
    private String status;
    /**
     *排序权重
     */
    @Field("smallint")
    private Long sort;
    /**
     *备注说明
     */
    @Field("desc")
    private String desc;
    /**
     *创建人
     */
    @Field("create_by")
    private String createBy;
    /**
     *创建时间
     */
    @Field("create_at")
    private Date createAt;
    /**
     *所属组织
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     *是否默认 1是 0否
     */
    @Field("is_default")
    private String isDefault;
    /**
     *权限类型
     */
    @Field("type")
    private String type;
}
