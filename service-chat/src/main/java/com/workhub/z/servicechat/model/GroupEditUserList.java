package com.workhub.z.servicechat.model;

import lombok.Data;

/**
*@Description: 群成员编辑人员信息
*@Author: 忠
*@date: 2019/8/30
*/
@Data
public class GroupEditUserList {
        private String id;
        private String name;
        private String avatar;
        private int deleted;
        private String secretLevel;
        private String pid;
        private String key;
        private String title;
}
