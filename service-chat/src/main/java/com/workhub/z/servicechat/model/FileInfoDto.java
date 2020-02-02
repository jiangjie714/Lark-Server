package com.workhub.z.servicechat.model;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//@ApiModel(description = "文件信息模板")
public class FileInfoDto {

//    @ApiModelProperty("文件地址")
    private String path;

//    @ApiModelProperty("文件名称")
    private String fileName;

//    @ApiModelProperty("文件类型")
    private String Type;

//    @ApiModelProperty("文件最后修改日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
