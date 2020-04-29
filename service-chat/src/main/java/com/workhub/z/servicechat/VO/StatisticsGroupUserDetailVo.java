package com.workhub.z.servicechat.VO;
import lombok.Data;

import java.io.Serializable;

/**
 * @auther: zhuqz
 * @date: 2020/3/29 09:51
 * @description: 群成员统计详细
 */
@Data
public class StatisticsGroupUserDetailVo implements Serializable {

    private static final long serialVersionUID = 1193394788381257197L;
    /**群成员名称*/
    private String name;
    /**组织编码*/
    private String orgCode;
    /**路径*/
    private String path;
}