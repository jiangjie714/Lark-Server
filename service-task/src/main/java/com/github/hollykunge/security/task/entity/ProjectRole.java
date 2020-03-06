package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-3-3
 * @deprecation task_project_auth 角色
 */
@Data
@Document(collection="task_project_auth")
public class ProjectRole extends BaseEntity{

    /**
     *角色id
     */
    @Field("role_id")
    private String roleId;

    /**
     *角色名称
     */
    @Field("title")
    private String title;

    /**
     * 角色对应的权限资源列表
     */
    @Field("element_list")
    private List<ProjectElement> elementList;
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
     *所属组织
     */
    @Field("organization_code")
    private String organizationCode;
    /**
     * 所属团队
     * fansq 后续补充字段 20-3-4
     */
    @Field("team_code")
    private String teamCode;
    /**
     *是否默认 1是 0否
     */
    @Field("is_default")
    private String isDefault;
}
