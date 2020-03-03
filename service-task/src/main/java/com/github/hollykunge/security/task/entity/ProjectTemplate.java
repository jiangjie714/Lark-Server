package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @deprecation task_project_template 项目类型
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_template")
public class ProjectTemplate {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     *类型名称
     */
    @Field("name")
    private String name;
    /**
     *备注
     */
    @Field("description")
    private String description;
    /**
     *排序
     */
    @Field("sort")
    private Long sort;
    /**
     *创建时间
     */
    @Field("create_time")
    private Date createTime;
    /**
     *编号
     */
    @Field("code")
    private String code;
    /**
     *组织id
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     *封面
     */
    @Field("cover")
    private String cover;
    /**
     *创建人
     * todo  我感觉可以直接写对象信息 内嵌文档 减少多表关联
     * todo  例如 private User usr
     */
    @Field("member_code")
    private String memberCode;
    /**
     *系统默认
     */
    @Field("is_system")
    private String isSystem;
}
