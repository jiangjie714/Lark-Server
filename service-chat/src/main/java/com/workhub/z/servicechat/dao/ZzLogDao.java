package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.entity.config.ZzChatLog;
import tk.mybatis.mapper.common.Mapper;

public interface ZzLogDao  extends Mapper<ZzChatLog>{
    int log(ZzChatLog zzChatLog);
}
