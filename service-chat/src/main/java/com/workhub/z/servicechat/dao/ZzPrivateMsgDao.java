package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.PrivateFileVo;
import com.workhub.z.servicechat.entity.message.ZzPrivateMsg;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 私人消息(ZzPrivateMsg)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
public interface ZzPrivateMsgDao extends Mapper<ZzPrivateMsg> {
    //查询私有聊天文件
    //query文件名称
    List<PrivateFileVo> getFileList(@Param("userId") String userId, @Param("receiverId") String receiverId, @Param("query") String query);
}