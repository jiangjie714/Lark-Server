package com.workhub.z.servicechat.VO;

import lombok.Data;

//文件监控前端返回格式
@Data
public class FileMonitoringVo {
    //文件id
    private String fileId;

    //文件名称
    private String fileName;

    //文件路径
    private String filePath;

    //文件大小（M）
    private String fileSize;

    //上传时间 yyyy-MM-dd hh:mm:ss
    private String uploadTime;

    //上传人姓名
    private String uploadUserName;

    //文件密级
    private String levels;

    //群/私/会议 文件 0私人1群 905会议
    private String isGroup;
    //接收姓名
    private String receiverName;
}
