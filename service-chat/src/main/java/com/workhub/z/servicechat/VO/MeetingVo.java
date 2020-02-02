package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @author:zhuqz
 * description:会议
 * date:2019/9/20 10:39
 **/
@Data
public class MeetingVo {
    /**会议id*/
    private  String id    ;
    /**会议名称*/
    private  String name    ;
    /**会议描述*/
    private  String meetDescribe  ;
    /**创建时间*/
    private String createTime ;
    /**创建人id*/
    private  String creator    ;
    /**创建人名字*/
    private  String creatorName   ;
    /**创建人ip*/
    private String creatorIp;
    /**项目名称*/
    private  String pname    ;
    /**参与范围*/
    private  String scop    ;
    /**会议密级*/
    private  String secretLevel    ;
    /**会议头像*/
    private  String meetImg    ;
    /**会议跨场所类型*/
    private  String iscross    ;
    /**会议状态*/
    private  String status    ;
    /**会议类型*/
    private  String type    ;
    /**会议资源（结果）*/
    private  String meetResource    ;
    /**计划开始时间*/
    private  String startTime    ;
    /**计划结束时间*/
    private  String endTime    ;
    /**会议组织编码*/
    private  String org    ;
    /**会议当前议程 index code name */
    private  MeetingCurrentProgressVo currentProgress    ;
    /**会议所有议程 议程编号集合，逗号分割*/
    private  List<GeneralCodeNameVo> allProgress    ;
    /**会议成员人数*/
    private String memberNum;
    /**会议成员列表*/
    private List<MeetUserVo> meetMemberList;
}
