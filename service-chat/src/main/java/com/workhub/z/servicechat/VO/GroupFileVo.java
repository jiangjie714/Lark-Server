package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 @author zhuqz
 des:文件列表
 */
@Data
public class GroupFileVo {

    /**文件id*/
    private String fileId;

    /**文件名称*/
    private String fileName;

    /**上传时间 yyyy-MM-dd hh:mm:ss*/
    private String time;

    /**上传人*/
    private String reviser;

    /**文件密级*/
    private String levels;

    /**会议文件类型 只有当文件是会议文件才有效（isGroup=905）9000参评 9001参考 9002结论*/
    private String meetFileType;
    /**大小字节*/
    private String fileSize;
    /**会议文件类型 只有当文件是会议文件才有效（isGroup=905）9000参评 9001参考 9002结论*/
    private String extension;

}
