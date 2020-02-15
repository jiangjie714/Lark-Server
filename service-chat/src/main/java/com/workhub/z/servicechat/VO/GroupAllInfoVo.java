package com.workhub.z.servicechat.VO;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.workhub.z.servicechat.entity.ZzGroup;
import lombok.Data;

import java.util.List;

@Data
public class GroupAllInfoVo {
    private ZzGroup groupInfo;
    private List<AdminUser> adminUserList;
}
