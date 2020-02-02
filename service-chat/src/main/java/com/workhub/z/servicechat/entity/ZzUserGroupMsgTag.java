package com.workhub.z.servicechat.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 用户标记群消息
 *
 * @author zhuqz
 * @since 2019-06-11
 */
public class ZzUserGroupMsgTag  implements Serializable {

    private static final long serialVersionUID = 3166408018029598486L;//主键
    private String id;
    //用户id
    private String userId;
    //群组id
    private String groupId;
    //消息id
    private String mesId;
    //标记类型
    private String tagType;
    //创建时间
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMesId() {
        return mesId;
    }

    public void setMesId(String mesId) {
        this.mesId = mesId;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ZzUserGroupMsgTag{" +
                "id='" + id + '\'' +
                "groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", mesId='" + mesId + '\'' +
                ", tagType='" + tagType + '\'' +
                ", createTime=" + createTime +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
