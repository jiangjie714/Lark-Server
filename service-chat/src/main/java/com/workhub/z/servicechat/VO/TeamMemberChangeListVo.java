package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/3/3 19:51
 * @description: 群或者会议，人员变化列表
 */
@Data
public class TeamMemberChangeListVo {
    //新增的人员
    private List<String> addList;
    //删除的人员
    private List<String> delList;
}