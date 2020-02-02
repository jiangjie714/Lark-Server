package com.workhub.z.servicechat.model;
import lombok.Data;

import java.util.List;

@Data
public class GroupEditDto {
    private String groupId;
    private String type;
    private List<GroupEditUserList> userList ;

}
