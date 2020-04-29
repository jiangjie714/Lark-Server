package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.GroupStatusVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzGroupStatusDao;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.model.GroupStatusDto;
import com.workhub.z.servicechat.service.ZzGroupStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:群（会议）变更日志记录
 * date:2019/8/26 16:50
 **/
@Service("zzGroupStatusService")
public class ZzGroupStatusServiceImpl implements ZzGroupStatusService {
    private static Logger log = LoggerFactory.getLogger(ZzGroupStatusServiceImpl.class);
    @Resource
    ZzGroupStatusDao zzGroupStatusDao;
    @Override
    public int add(ZzGroupStatus zzGroupStatus){
        log.info("service接收到群(会议)变更消息："+ JSONObject.toJSONString(zzGroupStatus));
        return this.zzGroupStatusDao.add(zzGroupStatus);
    }
    /*
    groupId 群id
    operator 操作人
    groupName 群名称
    operatorName 操作人姓名
    operateType 操作类型
    timeBegin 开始时间
    timeEnd 结束时间
    */
    @Override
    public TableResultResponse<GroupStatusVo> query(Map param) throws Exception{
           String pageSize = Common.nulToEmptyString(param.get("pageSize"));
           if("".equals(pageSize)){
               pageSize = "10";
           }

           String pageNum = Common.nulToEmptyString(param.get("pageNo"));

            if("".equals(pageNum)){
                pageNum = "1";
            }
        /**默认是群日志*/
        String typeKey = "type";
        if("".equals(Common.nulToEmptyString(param.get(typeKey)))){
            String groupTypeValue = "0";
            param.put(typeKey,groupTypeValue);
        }
           PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
           List<GroupStatusDto> listData = this.zzGroupStatusDao.query(param);
           List<GroupStatusVo> list = new ArrayList<>(16);
           for(int i=0;i<listData.size();i++){
               GroupStatusVo groupStatusVo = new GroupStatusVo();
               GroupStatusDto groupStatusDto = listData.get(i);
               Common.copyObject(groupStatusDto,groupStatusVo);
               Clob des = groupStatusDto.getDescribe();
               groupStatusVo.setDescribe(Common.ClobToString(des));
               list.add(groupStatusVo);
           }
           PageInfo pageInfo = new PageInfo<>(list);
           TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
            );
            return res;
    }
}
