package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.CodeMeetingFunctionVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.dao.ZzCodeMeetingFunctionDao;
import com.workhub.z.servicechat.entity.meeting.ZzCodeMeetingFunction;
import com.workhub.z.servicechat.service.ZzCodeMeetingFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议功能菜单
 * date:2019/9/23 14:58
 **/
@Service("zzCodeMeetingFunctionService")
public class ZzCodeMeetingFunctionServiceImpl implements ZzCodeMeetingFunctionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzCodeMeetingFunctionDao zzCodeMeetingFunctionDao;
    /**新增*/
    @Override
    public  String add(ZzCodeMeetingFunction zzCodeMeetingFunction){
        zzCodeMeetingFunction.setId(RandomId.getUUID());
        try {
            Common.putVoNullStringToEmptyString(zzCodeMeetingFunction);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
        }
        Map params = new HashMap(16);
        params.put("code",zzCodeMeetingFunction.getCode());
        params.put("name",zzCodeMeetingFunction.getName());
        String codeNameCnt = "0";
        if(!this.zzCodeMeetingFunctionDao.ifCodeNameExists(params).equals(codeNameCnt)){
            return "0";
        }
        int i = this.zzCodeMeetingFunctionDao.add(zzCodeMeetingFunction);
        return zzCodeMeetingFunction.getId();
    }
    /**删除 根据code 或者 id*/
    @Override
    public  int deleteData(Map param){
        String keyId = "id";
        String keyCode = "code";
        if("".equals(Common.nulToEmptyString(param.get(keyId))) && "".equals(Common.nulToEmptyString(param.get(keyCode)))){
            return -2;
        }
        return  this.zzCodeMeetingFunctionDao.deleteData(param);
    }
    /**更新*/
    @Override
    public  int update(ZzCodeMeetingFunction zzCodeMeetingFunction){
        return this.zzCodeMeetingFunctionDao.update(zzCodeMeetingFunction);
    }
    /**查询 name编码名称、isUse是否使用中、pageSize、pageNo页数页码*/
    @Override
    public  TableResultResponse query(Map param){
        String pageSize = Common.nulToEmptyString(param.get("pageSize"));
        if("".equals(pageSize)){
            pageSize = "10";
        }

        String pageNum = Common.nulToEmptyString(param.get("pageNo"));

        if("".equals(pageNum)){
            pageNum = "1";
        }
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<CodeMeetingFunctionVo> list = this.zzCodeMeetingFunctionDao.query(param);
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
    /**根据codes批量查询*/
    @Override
    public List<CodeMeetingFunctionVo> queryByCodes(String codes){
        return this.zzCodeMeetingFunctionDao.queryByCodes(codes);
    }
}
