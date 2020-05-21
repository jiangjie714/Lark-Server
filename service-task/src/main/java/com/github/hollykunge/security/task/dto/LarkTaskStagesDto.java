package com.github.hollykunge.security.task.dto;

import com.github.hollykunge.security.task.entity.LarkTask;
import com.github.hollykunge.security.task.entity.LarkTaskStages;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-4-17
 * @deprecation 用于给前端返回数据 项目 关联 任务列表 关联 具体任务
 */
@Data
public class LarkTaskStagesDto extends LarkTaskStages {

    private String name;

    private String projectCode;

    private Integer sort;

    private String descrption;

    private String code;

    private Integer deleted;

    private String id;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private Date updTime;

    private String updUser;

    private String updName;

    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private List<LarkTaskDto> larkTasks;

}
