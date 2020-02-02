package com.workhub.z.servicechat.VO;

/**
 * author:zhuqz
 * description:
 * date:2019/9/16 16:08
 **/
public class GroupMemberVo {
    private String userName;//用户名称
    private String userId;//用户id
    private String userLevel;//用户密级
    private String userImg;//用户头像
    private String userNo;//用户身份证号

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
}
