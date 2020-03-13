package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:04
 * @description: 群信息统计
 */
@Data
public class StatisticsGroupVo {
    private String pathName;//所属组织
    private String groupName;//名称
    private String crtName;//创建人
    private String isCross;//0科室内1跨科室2跨场所
    private String msgCount;//发消息总数
}