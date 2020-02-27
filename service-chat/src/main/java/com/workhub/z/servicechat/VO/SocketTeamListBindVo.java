package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @author:zhuqz
 * description: 某个人员绑定的群体列表
 * date:2019/12/31 14:14
 **/
@Data
public class SocketTeamListBindVo {
    //群体的id列表（如会议、群组等等）
    private List<String> teamList;
    //绑定群体的人员
    private String userId;
}
