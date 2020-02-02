package com.workhub.z.servicechat.VO;

import java.util.List;

/**
 * @author:zhuqz
 * description:
 * date:2019/9/13 13:20
 **/
public class GroupApproveLogVo {
    private String id;
    private String operator;//操作人id
    private String operatorNo;//操作人身份证
    private String operateType;//动作300新建群，301新增成员，302删除成员，303群解散，304上传群文件,305审批群 后续跟进需要补充
    private String operateTime;//操作时间
    private String groupId;//群id
    private String groupName;//群名称
    private String groupLevel;//群密级
    private String groupPro;//群项目
    private String groupScope;//参与范围
    private String groupDes;//群描述
    private String groupType;//群类型0普通1跨科室2跨场所
    private String approve;//审批人id
    private String approveName;//审批人姓名
    private String approveRes;//审批结论1通过2不通过
    //操作人ip
    private String ip;
    private String status;//操作是否成功
    private String operatorName;//操作人姓名
    private List<GroupMemberVo> memberList;//群成员
    //类型 0群1会议
    private String type;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
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

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    public String getGroupPro() {
        return groupPro;
    }

    public void setGroupPro(String groupPro) {
        this.groupPro = groupPro;
    }

    public String getGroupScope() {
        return groupScope;
    }

    public void setGroupScope(String groupScope) {
        this.groupScope = groupScope;
    }

    public String getGroupDes() {
        return groupDes;
    }

    public void setGroupDes(String groupDes) {
        this.groupDes = groupDes;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getApproveRes() {
        return approveRes;
    }

    public void setApproveRes(String approveRes) {
        this.approveRes = approveRes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<GroupMemberVo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<GroupMemberVo> memberList) {
        this.memberList = memberList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
