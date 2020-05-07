package com.workhub.z.servicechat.VO;

public class PrivateFileVo {
    //文件id
    private String fileId;

    //文件名称
    private String fileName;

    //上传时间 yyyy-MM-dd hh:mm:ss
    private String time;

    //上传人
    private String reviser;
    //上传人姓名
    private String reviserName;
    //文件密级
    private String levels;

    //接收人
    private String receiver;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReviser() {
        return reviser;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReviserName() {
        return reviserName;
    }

    public void setReviserName(String reviserName) {
        this.reviserName = reviserName;
    }
}
