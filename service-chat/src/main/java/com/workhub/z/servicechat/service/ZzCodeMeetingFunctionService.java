package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.CodeMeetingFunctionVo;
import com.workhub.z.servicechat.entity.meeting.ZzCodeMeetingFunction;

import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议功能菜单
 * date:2019/9/23 14:58
 **/
public interface ZzCodeMeetingFunctionService {
    String add(ZzCodeMeetingFunction zzCodeMeetingFunction);
    int deleteData(Map param);
    int update(ZzCodeMeetingFunction zzCodeMeetingFunction);
    TableResultResponse query(Map param);
    List<CodeMeetingFunctionVo> queryByCodes(String codes);
}
