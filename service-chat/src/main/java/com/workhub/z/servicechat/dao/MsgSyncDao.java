package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.entity.message.ZzMessageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MsgSyncDao {
    //获取待同步列表
    List<ZzMessageInfo> getSyncMsgList();
    //同步消息
    int syncMsg(@Param("msg") ZzMessageInfo msg);
    //修改同步标记
    int updateSyncFlg(@Param("msgId") String msgId);
}
