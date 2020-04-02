package com.github.hollykunge.security.admin.api.authority;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 82455
 * @auther: zhuqz
 * @date: 2020/3/29 09:53
 * @description:群人员统计外层
 */
@Data
public class StatisticsGroupUserVo implements Serializable {
    private static final long serialVersionUID = -8705723784877034133L;
    /**群名称*/
    private  String groupName;
    /**群成员个数*/
    private int groupUsersCount;
    /**群成员列表*/
    private List<StatisticsGroupUserDetailVo> userList;
}