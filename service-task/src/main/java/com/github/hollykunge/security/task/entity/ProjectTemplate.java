package com.github.hollykunge.security.task.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @deprecation task_project_template 项目模板
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_template")
public class ProjectTemplate extends BaseEntity{

    /**
     *模板id
     */
    @Field("template_id")
    private String templateId;
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
    @Field("user_information")
    private ProjectMember userInformation;
    /**
     *系统默认
     */
    @Field("is_system")
    private String isSystem;
}
