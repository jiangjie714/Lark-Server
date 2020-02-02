package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.CodeMeetingFunctionVo;
import com.workhub.z.servicechat.entity.ZzCodeMeetingFunction;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议功能菜单
 * date:2019/9/23 14:55
 **/
public interface ZzCodeMeetingFunctionDao extends Mapper<ZzCodeMeetingFunction> {
    int add(@Param("params") ZzCodeMeetingFunction zzCodeMeetingFunction);
    int update(@Param("params") ZzCodeMeetingFunction zzCodeMeetingFunction);
    int deleteData(@Param("params") Map param);
    List<CodeMeetingFunctionVo>  query(@Param("params") Map param);
    List<CodeMeetingFunctionVo> queryByCodes(@Param("codes") String codes);
    String ifCodeNameExists(@Param("params") Map params);
}
