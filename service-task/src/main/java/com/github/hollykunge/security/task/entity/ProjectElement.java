package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 资源列表
 */
@Data
@Document(collection="task_project_element")
public class ProjectElement extends BaseEntity {

    /**
     * 资源id
     */
    @Field("element_id")
    private String elementId;

    /**
     * 资源编码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 资源类型
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 资源名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 资源路径
     */
    @Column(name = "URI")
    private String uri;

    /**
     *资源对应菜单id
     */
    @Column(name = "MENU_ID")
    private String menuId;

    /**
     *父级id
     */
    @Column(name = "PARENT_ID")
    private String parentId;

    /**
     *资源树状检索路径
     */
    @Column(name = "PATH")
    private String path;

    /**
     *资源请求类型
     */
    @Column(name = "METHOD")
    private String method;

    /**
     *资源描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    @Field("child_elementList")
    private List<ProjectElement> childElementList;
}