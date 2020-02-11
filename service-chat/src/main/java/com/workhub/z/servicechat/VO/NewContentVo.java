package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description: 返给前端vo
 * date:2019/12/4 16:12
 **/
@Data
public class NewContentVo {
    /**如果是附件，附件扩展名称*/
    private String extension;
    /**如果是附件，附件大小*/
    private String fileSize;
    /**如果是附件，附件id*/
    private String id;
    /**消息类型1文字2图片3附件*/
    private String type;
    /**消息内容或者附件名称*/
    private String title;
    /**消息密级*/
    private String secretLevel;
}
