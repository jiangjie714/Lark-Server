package com.workhub.z.servicechat.dao.config;

import com.workhub.z.servicechat.entity.config.ZzChatLog;
import tk.mybatis.mapper.common.Mapper;

public interface ZzLogDao  extends Mapper<ZzChatLog>{
    int log(ZzChatLog zzChatLog);
}
