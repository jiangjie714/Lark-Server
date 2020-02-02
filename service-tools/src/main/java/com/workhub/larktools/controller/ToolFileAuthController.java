package com.workhub.larktools.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.workhub.larktools.entity.ToolFileAuth;
import com.workhub.larktools.service.ToolFileAuthService;
import com.workhub.larktools.tool.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

/**
 * author:zhuqz
 * description:工具下载权限配置
 * date:2019/8/30 14:00
 **/
@RestController
@RequestMapping("/toolFileAuth")
public class ToolFileAuthController {
    private static Logger log = LoggerFactory.getLogger(ToolFileAuthController.class);
    @Resource
    ToolFileAuthService toolFileAuthService;
    @Resource
    private HttpServletRequest httpServletRequest;


    @PostMapping("/add")
    public ObjectRestResponse add(@RequestBody  ToolFileAuth toolFileAuth) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        toolFileAuth.setId(UUIDUtils.generateShortUuid());
        String userId = CommonUtil.nulToEmptyString(httpServletRequest.getHeader("userId"));
        String userName = URLDecoder.decode(CommonUtil.nulToEmptyString(httpServletRequest.getHeader("userName")),"UTF-8");
        toolFileAuth.setCreator(userId);
        CommonUtil.putEntityNullToEmptyString(toolFileAuth);
        int i = toolFileAuthService.add(toolFileAuth);
        return res;
    }

    /**
    * @MethodName: delete
     * @Description:
     * @Param: [param] fileId,orgCode
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/8/30
    **/
    @PostMapping("delete")
    public  ObjectRestResponse delete(@RequestBody Map param) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId = CommonUtil.nulToEmptyString(httpServletRequest.getHeader("userId"));
        String userName = URLDecoder.decode(CommonUtil.nulToEmptyString(httpServletRequest.getHeader("userName")),"UTF-8");
        param.put("userId",userId);
        int i = toolFileAuthService.delete(param);
        return res;
    }

    /**
    * @MethodName: ifAuthExists
     * @Description: 是否具有下载权限
     * @Param: [fileId, orgCode]
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse 1可以下载0不可以下载
     * @Author: zhuqz
     * @Date: 2019/8/30
    **/
    @GetMapping("ifAuthExists")
    public  ObjectRestResponse ifAuthExists(@RequestParam String fileId ,String orgCode) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String auth =  this.toolFileAuthService.ifAuthExists(fileId,orgCode);
        return res.data(auth);
    }
    /**
    * @MethodName: queryByFileId
     * @Description: 查询某个工具下载权限集合
     * @Param: [fileId]
     * @Return: com.github.hollykunge.security.common.msg.ListRestResponse
     * @Author: zhuqz
     * @Date: 2019/8/30
    **/
    @GetMapping("queryByFileId")
    public ListRestResponse queryByFileId(@RequestParam String fileId) throws Exception{
       return this.toolFileAuthService.queryByFileId(fileId);
    }
}
