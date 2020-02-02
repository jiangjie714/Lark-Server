package com.workhub.z.servicechat.VO;

import java.io.Serializable;

/**
 * author:zhuqz
 * description:
 * date:2019/8/26 15:58
 **/
public class GroupStatusVo implements Serializable {
    private static final long serialVersionUID = 7442013759551848255L;
    private String id;
    private String groupId;
    private String groupName;
    private String operator;
    private String operatorName;
    /**操作类型MessageType.FLOW...
     * 300新建群，301新增成员，302删除成员，303群解散，304上传群文件,305 下载群文件，306 群审批
     * 后续跟进需要补充*/
    private String operateType;
    private String operateTime;
    private String describe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
