package com.workhub.z.servicechat.VO;

import com.workhub.z.servicechat.entity.group.ZzGroup;
import lombok.Data;

import java.util.List;

@Data
public class GroupAllInfoVo {
    private ZzGroup groupInfo;
    private List<ChatAdminUserVo> adminUserList;
}
