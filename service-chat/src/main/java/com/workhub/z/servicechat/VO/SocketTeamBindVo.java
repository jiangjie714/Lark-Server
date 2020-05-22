package com.workhub.z.servicechat.VO;

import lombok.Data;

import java.util.List;

/**
 * @author:zhuqz
 * description: socket绑定群体的消息体
 * date:2019/12/30 10:58
 **/
@Data
public class SocketTeamBindVo {
    /**
     * 绑定（解绑）群体的人员列表
     */
    private List<String> userList;
    /**
     * 群体的id（如会议、群组等等）
     */
    private String teamId;
    /**
     * 绑定(解绑)某个群体后发送消息,如果不为空，不发送，只是单纯的绑定（解绑）
     */
    private SocketMsgDetailVo msg;
    /**
     * 绑定（解绑）后，全量发送还是增量发送
     * 该变量只有在msg不为空才有意义 true全量，false增量
     * 对于绑定 如果wholeFlg = true ，那么绑定后，会给绑定teamId发送群体消息 msg
     * 对于绑定 如果wholeFlg = false ，那么绑定后，会遍历userList发送单人消息 msg
     * 对于解绑 如果wholeFlg = true, 那么在解绑前，会先给teamId发送群体消息msg，然后解绑
     * 对于解绑 如果wholeFlg = false,那么解绑后，会遍历userList发送单人消息msg
     */
    private boolean wholeFlg = true;
}
