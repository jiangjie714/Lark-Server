package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @deprecation task_project_menu 项目菜单
 * @since 20-3-3
 * todo 我感觉这少一个 字段 存取图片的具体内容
 * todo 例如 private Binary imageContent；
 */
@Data
@Document(collection = "task_project_menu")
public class ProjectMenu {

    /**
     * id 是 mongodb的 objectId
     */
    @Id
    private String id;
    /**
     *父id
     */
    @Field("pid")
    private String pid;
    /**
     *名称
     */
    @Field("title")
    private String  title;
    /**
     *菜单图标
     */
    @Field("icon")
    private String  icon;
    /**
     *链接
     */
    @Field("url")
    private String  url;
    /**
     *文件路径
     */
    @Field("file_path")
    private String filePath;
    /**
     *链接参数
     */
    @Field("params")
    private String  params;
    /**
     *权限节点
     */
    @Field("node")
    private String  node;
    /**
     *菜单排序
     */
    @Field("sort")
    private Long sort;
    /**
     *状态(0:禁用,1:启用)
     */
    @Field("status")
    private String status;
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
     *是否内页
     */
    @Field("is_inner")
    private String isInner;
    /**
     *参数默认值
     */
    @Field("values")
    private String values;
    /**
     *是否显示侧栏
     */
    @Field("show_slider")
    private String showSlider;
}
