package com.workhub.z.servicechat.entity;

import java.io.Serializable;

/**
 * 提及（@）功能实现(ZzAt)实体类
 *
 * @author 忠
 * @since 2019-05-10 14:21:34
 */
public class ZzAt implements Serializable {
    private static final long serialVersionUID = 987759031096504823L;
    //id
    private String id;
    //群组ID
    private String groupid;
    //被@人ID
    private String receiverid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

}