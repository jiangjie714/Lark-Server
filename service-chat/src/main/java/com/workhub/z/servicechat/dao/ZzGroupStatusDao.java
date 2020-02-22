package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.model.GroupStatusDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description: 群(会议)流水日志
 * date:2019/8/26 16:01
 **/
public interface ZzGroupStatusDao  extends Mapper<ZzGroupStatus> {
     /**
      * 新增
      * @param zzGroupStatus
      * @return
      */
     int add(@Param("param") ZzGroupStatus zzGroupStatus);

     /**
      * 查询
      * @param params
      * @return
      */
     List<GroupStatusDto> query(@Param("param") Map params);
}
