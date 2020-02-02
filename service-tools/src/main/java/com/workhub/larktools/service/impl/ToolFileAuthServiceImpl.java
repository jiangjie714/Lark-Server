package com.workhub.larktools.service.impl;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.workhub.larktools.dao.ToolFileAuthDao;
import com.workhub.larktools.entity.ToolFileAuth;
import com.workhub.larktools.service.ToolFileAuthService;
import com.workhub.larktools.vo.ToolFileAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * author:zhuqz
 * description:
 * date:2019/8/30 14:39
 **/
@Service("toolFileAuthService")
public class ToolFileAuthServiceImpl implements ToolFileAuthService {
    @Resource
    private ToolFileAuthDao toolFileAuthDao;
    @Override
    public int add(ToolFileAuth toolFileAuth){
       return this.toolFileAuthDao.addData(toolFileAuth);
    }
    @Override
    public int delete(Map params){
        return this.toolFileAuthDao.deleteData(params);
    }
    @Override
    public String ifAuthExists(String fileId, String orgCode){
        return this.toolFileAuthDao.ifAuthExists(fileId,orgCode);
    }
    @Override
    public ListRestResponse queryByFileId(String fileId){
        List<ToolFileAuthVo> dataList = this.toolFileAuthDao.queryByFileId(fileId);
        return new ListRestResponse("200",dataList.size(),dataList);
    }
}
