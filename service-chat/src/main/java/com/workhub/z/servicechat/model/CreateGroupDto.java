package com.workhub.z.servicechat.model;

import java.util.List;

/**
*@Description: 创建群组Dto
*@Author: 忠
*@date: 2019/6/6
*/
public class CreateGroupDto {

    private String groupName;
    private String creator;
    private String groupDescribe;
    private String updator;
    private String pname;
    private String scop;
    private String levels;
    private String type;
    private List<UserListDto> userList;
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getCreator() {
        return creator;
    }

    public void setGroupDescribe(String groupDescribe) {
        this.groupDescribe = groupDescribe;
    }
    public String getGroupDescribe() {
        return groupDescribe;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }
    public String getUpdator() {
        return updator;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
    public String getPname() {
        return pname;
    }

    public void setScop(String scop) {
        this.scop = scop;
    }
    public String getScop() {
        return scop;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }
    public String getLevels() {
        return levels;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setUserList(List<UserListDto> userList) {
        this.userList = userList;
    }
    public List<UserListDto> getUserList() {
        return userList;
    }

}