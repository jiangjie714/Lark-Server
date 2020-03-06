package com.github.hollykunge.security.task.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author fansq
 * @deprecation task_project_report 项目报表统计
 * @since 20-3-3
 */
@Data
@Document(collection = "task_project_report")
public class ProjectReport extends BaseEntity{

    /**
     *报表id
     */
    @Field("report_id")
    private String reportId;
    /**
     *项目编号
     */
    @Field("project_code")
    private String projectCode;
    /**
     *日期
     */
    @Field("date")
    private Date date;
    /**
     *内容
     */
    @Field("content")
    private String content;
}
