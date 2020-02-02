package com.workhub.z.servicechat.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhuqz
 * description:会议表实体
 * date:2019/9/20 9:57
 **/
@Data
public class ZzMeeting implements Serializable {
    private static final long serialVersionUID = -873938958430693499L;
       /**会议id*/
       private  String id    ;
       /**会议名称*/
       private  String meetName    ;
   /**会议描述*/
    private  String meetDescribe  ;
   /**项目名称*/
    private  String pname    ;
   /**参与范围*/
    private  String scop    ;
   /**会议密级*/
    private  String levels    ;
   /**会议头像*/
    private  String meetImg    ;
   /**会议跨场所类型*/
    private  String iscross    ;
   /**会议状态*/
    private  String meetStatus    ;
   /**创建时间*/
    private Date crtTime;
   /**创建人*/
    private  String crtUser;
   /**创建人姓名*/
    private  String crtName;
   /**创建人ip*/
    private  String crtHost;
   /**更新时间*/
    private  String updTime;
   /**更新人*/
    private  String updUser;
   /**更新人姓名*/
    private  String updName;
   /**更新人ip*/
    private  String updHost;
   /**会议类型*/
    private  String meetType     ;
   /**会议资源*/
    private  String meetResource;
   /**开始时间*/
    private  Date beginTime;
   /**结束时间*/
    private  Date endTime;
   /**会议组织*/
    private  String org;
   /**当前议程 格式：下标,议程编号 例如 ： 1,5003 表示第一个议程，对应的编码5003*/
    private  String  currentProgress;
   /**所有可选择议程*/
    private  String  allProgress;

}
