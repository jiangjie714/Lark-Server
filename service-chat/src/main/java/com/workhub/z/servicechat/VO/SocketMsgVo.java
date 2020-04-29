package com.workhub.z.servicechat.VO;

import com.workhub.z.servicechat.config.SocketMsgTypeEnum;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author:zhuqz
 * description: 发送给消息给socket端
 * date:2019/12/27 16:02
 **/
@Data
public class SocketMsgVo implements Serializable {
    private static final long serialVersionUID = 3317552605396382844L;
    /**数据id 确认消息接收成功使用*/
    String id;
    /**发送人*/
    String sender = "system";
    /**接收人,多个逗号分割，且多个人情况下confirmFlg应是fasle*/
    String receiver;
    /**编码**/
    String code;
    /**消息内容**/
    SocketMsgDetailVo msg;
    /**是否需要应答*/
    Boolean confirmFlg = false;

    public String getCode() {
        return code;
    }

    public void setCode(SocketMsgTypeEnum code) {
        this.code = code.getCode();
    }
    public void setCode(String code) throws Exception{
        boolean check = false;
        for(SocketMsgTypeEnum item : SocketMsgTypeEnum.values()){
            if(item.getCode().equals(code)){
                check = true;
                break;
            }
        }
        if(!check){
            throw new Exception("编码不再枚举范围内："+code);

        }
        this.code = code;
    }
}
