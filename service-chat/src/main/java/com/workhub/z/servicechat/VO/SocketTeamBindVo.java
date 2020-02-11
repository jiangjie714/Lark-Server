package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @author:zhuqz
 * description: socket绑定群体的消息体
 * date:2019/12/30 10:58
 **/
@Data
public class SocketTeamBindVo {
    //绑定群体的人员列表
    private List<String> userList;
    //群体的id（如会议、群组等等）
    private String teamId;
}
