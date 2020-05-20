package com.workhub.z.servicechat.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class GroupTaskDto<T> {
    //群组标识
    private String groupId;

    //时间戳
    private Date timestamp;

    //修改人
    private String reviser;

    //操作类型，0群创建，1，加入群组；2，退出群组；3.关闭群组；4.删除群组；5群编辑
    private int type;

    // 用户列表
    private List<UserListDto> userList;

    //创建群组po(ZzGroup/MeetingDto)
    private T zzGroup;
}
