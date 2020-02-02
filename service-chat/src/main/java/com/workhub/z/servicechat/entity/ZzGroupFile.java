package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群文件(ZzGroupFile)实体类
 *
 * @author 忠
 * @since 2019-05-13 10:59:08
 */
@Data
public class ZzGroupFile implements Serializable {
    private static final long serialVersionUID = -66487084457900840L;
    //主键
    private String id;
    //附件id
    private String fileId;
    //附件名称
    private String fileName;
    //文件后缀名
    private String fileExt;
    //附件类型（text、img、doc）
    private String fileType;
    //文件大小
    private Double sizes;
    //附件路径
    private String path;
    //附件转码为可读取的路径
    private String readPath;
    //创建时间
    private Date createTime;
    //创建人
    private String creator;
    //更新时间
    private Date updateTime;
    //更新人
    private String updator;
    //组id
    private String groupId;
    //保密等级
    private String levels;
    //创建人姓名
    private String creatorName;
    //审计标志
    private String approveFlg;
    //接收人姓名
    private  String receiverName;
    /**是否群组文件1群文件0私聊文件905会议文件*/
    private  String isGroup;
    /**会议文件类型（isGroup=905）9000评审文件9001结论文件等等*/
    private String meetFileType;
}