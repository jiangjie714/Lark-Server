package com.workhub.z.servicechat.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户群组映射表(ZzUserGroup)实体类
 *
 * @author 忠
 * @since 2019-05-10 14:22:53
 */
public class ZzUserGroup implements Serializable {
    private static final long serialVersionUID = 130845560887372836L;
    //主键
    private String id;
    //用户id
    private String userId;
    //群组id
    private String groupId;
    //创建时间
    private Date createtime;


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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}