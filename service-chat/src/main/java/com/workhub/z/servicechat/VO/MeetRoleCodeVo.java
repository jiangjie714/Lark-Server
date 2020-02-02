package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * author:zhuqz
 * description: 会议角色代码表
 * date:2019/9/26 16:27
 **/
@Data
public class MeetRoleCodeVo extends GeneralCodeNameVo{
    /**该角色有哪些功能菜单*/
    private List<GeneralCodeNameVo> functionList;
}
