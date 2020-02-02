package com.workhub.larktools.service.impl;

import com.github.hollykunge.security.api.vo.user.UserInfo;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.workhub.larktools.dao.ToolOrgDao;
import com.workhub.larktools.entity.ToolTree;
import com.workhub.larktools.feign.IUserService;
import com.workhub.larktools.service.ToolOrgService;
import com.workhub.larktools.tool.CommonUtil;
import com.workhub.larktools.vo.ToolTreeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.workhub.larktools.feign.IUserService;

/**
 * author:zhuqz
 * description:
 * date:2019/8/13 15:07
 **/
@Service("toolOrgService")
public class ToolOrgServiceImpl implements ToolOrgService {
    private static Logger log = LoggerFactory.getLogger(ToolOrgServiceImpl.class);
     @Resource
     private ToolOrgDao toolOrgDao;
    @Resource
    private IUserService iUserService;

    @Override
    @Transactional
    public  String add(Map param){

        String treeType = CommonUtil.nulToEmptyString(param.get("treeType"));
        if("".equals(treeType)){
            param.put("treeType","000");
        }
        param.put("id", UUIDUtils.generateShortUuid());
        String pid = CommonUtil.nulToEmptyString(param.get("pid"));
        if("".equals(pid) || "-1".equals(pid)){//根节点

            param.put("pid","-1");
            //调用feign接口获取用户组织信息
            String userId = param.get("userId").toString();
            //userId = "yanzhenqing";
            UserInfo userInfo = iUserService.info(userId);
            if(userInfo == null){
                return "-1";
            }
            String orgCodeFull = userInfo.getOrgCode();
            if(orgCodeFull == null || orgCodeFull.equals("")){
                return "-1";
            }
            String orgCode = orgCodeFull.substring(0,4);
            String cnt  = this.toolOrgDao.queryExistsTypeTree(param.get("treeType").toString(),orgCode);

            if(!cnt.equals("0")){
                return "0";
            }

            Map orgParam = new HashMap();
            orgParam.put("id", UUIDUtils.generateShortUuid());
            orgParam.put("userId",userId);
            orgParam.put("treeId",param.get("id"));
            orgParam.put("orgCode",orgCode);
            toolOrgDao.addTreeOrg(orgParam);
        }

        String order = CommonUtil.nulToEmptyString(param.get("order"));
        if("".equals(order)){
            param.put("order","0");
        }

        int i = this.toolOrgDao.add(param);

        return CommonUtil.nulToEmptyString(param.get("id"));
    }
    @Override
    public  int update(Map param){
        return this.toolOrgDao.update(param);
    }
    @Override
    public  int updateTreeNodeParent(Map param){
        return this.toolOrgDao.updateTreeNodeParent(param);
    }
    @Override
    @Transactional
    public  int delete(Map param){
        ToolTree node = this.toolOrgDao.querySingleNode(param);
        //如果是根节点，删除树和组织结构关联信息
        if(node!=null && node.getPid().equals("-1")){
            Map newparams = new HashMap();
            newparams.put("treeId",param.get("id"));
            newparams.put("userId",param.get("userId"));
            this.toolOrgDao.deleteTreeOrg(newparams);
        }
        return this.toolOrgDao.deleteNode(param);
    }
    /**
    * @MethodName: queryCurrentUserTree
     * @Description: 获取当前用户的树结构
     * @Param: []
     * @Return: com.github.hollykunge.security.common.msg.ListRestResponse<com.workhub.larktools.vo.ToolTreeVo>
     * @Author: zhuqz
     * @Date: 2019/8/15
    **/
    @Override
    public  ListRestResponse<ToolTreeVo> queryCurrentUserTree(String userId){
        //userId = "yanzhenqing";
        UserInfo userInfo = this.iUserService.info(userId);
        if(userInfo==null){
            return  null;
        }
        List<ToolTreeVo> dataList = this.toolOrgDao.queryCurrentUserTree(userInfo.getOrgCode());
        return   new ListRestResponse("200",dataList.size(),dataList);
    }

    @Override
    public  ListRestResponse<ToolTreeVo> query(Map param){
        List<ToolTreeVo> dataList = this.toolOrgDao.query(param);
        return   new ListRestResponse("200",dataList.size(),dataList);
    }
}
