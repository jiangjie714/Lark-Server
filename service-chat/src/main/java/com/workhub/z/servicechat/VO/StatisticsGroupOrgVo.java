package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/3/31 13:19
 * @description: 群统计orgcode
 */
@Data
public class StatisticsGroupOrgVo implements Serializable {
    private static final long serialVersionUID = 1193394788381257199L;
    /**
     * 群名称
     */
    private String groupName;
    /**
     * org个数
     */
    private int groupOrgCount;
    /**
     * org列表
     */
    private List<StatisticsGroupOrgDetailVo> orgList;
}