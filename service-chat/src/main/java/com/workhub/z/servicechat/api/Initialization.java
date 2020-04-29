package com.workhub.z.servicechat.api;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.vo.rpcvo.ContactVO;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
*@Description: 系统用户初始化类
*@Author: 忠
*@date: 2019/3/21
*/
@Controller
@RequestMapping("/Initialization")
public class Initialization {

    @Autowired
    private AdminUserService iUserService;

    @Autowired
    private ZzUserGroupService zzUserGroupService;
    /**
    *@Description: 查询user所在群组
    *@Param: userid
    *@return: user群组列表
    *@Author: 忠
    *@date: 2019/3/21
    */
    public JSONObject queryGroupByUserId(String userId){


        return null;
    }
    /**
    *@Description: 群文件
    *@Param: 
    *@return: 
    *@Author: 忠
    *@date: 2019/6/10
    */
    
    /**
    *@Description: 查询用户详细信息
    *@Param: userid
    *@return: usermodel
    *@Author: 忠0
    *@date: 2019/3/21
    */
    @RequestMapping("/getInfo")
    public void queryUserInfoBySn(String sn){
    }

    /**
    *@Description: 最近联系人
    *@Param:userid
    *@return:Contact
    *@Author: 忠
    *@date: 2019/3/21
    */
    @GetMapping("/getContact")
    @ResponseBody
    public ListRestResponse queryContactById(@RequestParam("id")String id){
        //
        List<ContactVO> list = this.zzUserGroupService.getContactVOList(id);
        return new ListRestResponse("处理完成",list.size(),list);
    }
}
