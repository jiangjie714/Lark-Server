package com.github.hollykunge.security.task.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author fansq
 * @since 20-4-30
 * @deprecation  这个实体仅限于 task 页面搜索使用
 * 封装搜索条件
 */
@Data
public class LarkTaskVO implements Serializable {

    //id
    private String id;
    //是否完成
    private Integer done;
    //项目id
    private String projectCode;
    //任务列id
    private String stagesCode;
    //任务名称
    private String name;
    //默认父任务
    private String pCode="0";
    //紧急程度
    private Integer pri;
    //项目状态
    private String status;
    //完成时间
    private String doneTime;
    //创建时间
    private String crtTime;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;
    //执行者
    private List<String> executors;
    //创建者
    private List<String> creators;
    //参与者
    private List<String> joiners;


}
