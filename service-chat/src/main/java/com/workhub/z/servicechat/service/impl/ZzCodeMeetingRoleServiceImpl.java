package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.CodeMeetingFunctionVo;
import com.workhub.z.servicechat.VO.CodeMeetingRoleVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.dao.ZzCodeMeetingFunctionDao;
import com.workhub.z.servicechat.dao.ZzCodeMeetingRoleDao;
import com.workhub.z.servicechat.entity.meeting.ZzCodeMeetingRole;
import com.workhub.z.servicechat.service.ZzCodeMeetingRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议角色代码表
 * date:2019/9/23 14:46
 **/
@Service("zzCodeMeetingRoleService")
public class ZzCodeMeetingRoleServiceImpl implements ZzCodeMeetingRoleService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzCodeMeetingRoleDao zzCodeMeetingRoleDao;
    @Resource
    private ZzCodeMeetingFunctionDao zzCodeMeetingFunctionDao;
    /**新增*/
    @Override
    public  String add(ZzCodeMeetingRole zzCodeMeetingRole){
        zzCodeMeetingRole.setId(RandomId.getUUID());
        try {
            Common.putVoNullStringToEmptyString(zzCodeMeetingRole);
        }catch (Exception e){
            logger.error(Common.getExceptionMessage(e));
        }
        Map params = new HashMap(16);
        params.put("code",zzCodeMeetingRole.getCode());
        params.put("name",zzCodeMeetingRole.getName());
        String codeNameNum = "0";
        if(!codeNameNum.equals(this.zzCodeMeetingRoleDao.ifCodeNameExists(params))){
            return "0";
        }
        int i = this.zzCodeMeetingRoleDao.add(zzCodeMeetingRole);
        return zzCodeMeetingRole.getId();
    }
    /**删除 根据code 或者 id*/
    @Override
    public  int deleteData(Map param){
        String keyId = "id";
        String keyCode = "code";
        if("".equals(Common.nulToEmptyString(param.get(keyId))) && "".equals(Common.nulToEmptyString(param.get(keyCode)))){
            return -2;
        }
        return  this.zzCodeMeetingRoleDao.deleteData(param);
    }
    /**更新*/
    @Override
    public  int update(ZzCodeMeetingRole zzCodeMeetingRole){
        return this.zzCodeMeetingRoleDao.update(zzCodeMeetingRole);
    }
    /**查询 name编码名称、isUse是否使用中、pageSize、pageNo页数页码*/
    @Override
    public TableResultResponse query(Map param){
        String pageSize = Common.nulToEmptyString(param.get("pageSize"));
        if("".equals(pageSize)){
            pageSize = "10";
        }

        String pageNum = Common.nulToEmptyString(param.get("pageNo"));

        if("".equals(pageNum)){
            pageNum = "1";
        }
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map> list = this.zzCodeMeetingRoleDao.query(param);
        PageInfo pageInfo = new PageInfo<>(list);
        List<CodeMeetingRoleVo> dataList = new ArrayList<>();
        for(Object obj : pageInfo.getList()){
            Map map = (Map)obj;
            CodeMeetingRoleVo codeMeetingRoleVo = new CodeMeetingRoleVo();
            codeMeetingRoleVo.setId(Common.nulToEmptyString(map.get("ID")));
            codeMeetingRoleVo.setCode(Common.nulToEmptyString(map.get("CODE")));
            codeMeetingRoleVo.setName(Common.nulToEmptyString(map.get("NAME")));
            codeMeetingRoleVo.setIsUse(Common.nulToEmptyString(map.get("ISUSE")));
            codeMeetingRoleVo.setCrtHost(Common.nulToEmptyString(map.get("CRTHOST")));
            codeMeetingRoleVo.setCrtName(Common.nulToEmptyString(map.get("CRTNAME")));
            codeMeetingRoleVo.setCrtUser(Common.nulToEmptyString(map.get("CRTUSER")));
            codeMeetingRoleVo.setCrtTime(Common.nulToEmptyString(map.get("CRTTIME")));
            String functions = Common.nulToEmptyString(map.get("MEETFUNCTION"));
            if(!"".equals(functions)){

                String newFunctions = "";
                String[] functionArr = functions.split(",");
                for(String function : functionArr){
                    newFunctions+=(",'"+function+"'");
                }
                newFunctions = newFunctions.substring(1);
                List<CodeMeetingFunctionVo> functionVos = this.zzCodeMeetingFunctionDao.queryByCodes(newFunctions);
                codeMeetingRoleVo.setMeetFunction(functionVos);
                dataList.add(codeMeetingRoleVo);
            }
        }
        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                dataList
        );
        return res;
    }
    /**代码是否存在*/
    @Override
    public boolean ifRoleCodeExist(String roleCode){
        String roleCodeCnt = "0";
        if(roleCodeCnt.equals(this.zzCodeMeetingRoleDao.getRoleCodeCnt(roleCode))){
            return false;
        }
        return  true;
    }
}
