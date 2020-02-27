package com.workhub.z.servicechat.VO;

import com.workhub.z.servicechat.entity.ZzContactInf;
import lombok.Data;

/**
 * @author:zhuqz
 * description: 返回给前端消息vo
 * date:2019/12/4 14:59
 **/
@Data
public class NewMessageVo {
    /**消息id*/
    private String id;
    //发送人
    private NewContactVo sender;
    //联系人
    private NewContactVo contactor;
    /**发送时间 yyyy-MM-dd HH:mm:ss*/
    private String sendTime;
    /**消息群体类型：USER私人消息 GROUP群组消息 MEET会议消息*/
    //private String teamType;
    /**消息跨场所属性0科室内1跨科室2跨场所*/
    //private String cross;
    /**消息具体内容*/
    private NewContentVo msgContent;

}
