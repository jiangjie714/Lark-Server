package com.workhub.z.servicechat.controller.meeting;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.meeting.ZzCodeMeetingRole;
import com.workhub.z.servicechat.service.ZzCodeMeetingRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议角色代码表
 * date:2019/9/23 14:45
 **/
@RestController
@RequestMapping("zzCodeMeetingRole")
public class ZzCodeMeetingRoleController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ZzCodeMeetingRoleService zzCodeMeetingRoleService;
    @Resource
    private HttpServletRequest request;
    /**
     * @MethodName: add
     * @Description: 新增
     * @Param: [ZzCodeMeetingRole]
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse 返回id
     * @Author: zhuqz
     * @Date: 2019/9/20
     **/
    @PostMapping("add")
    public ObjectRestResponse add(@RequestBody ZzCodeMeetingRole zzCodeMeetingRole) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        String userId= common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
        try {
            zzCodeMeetingRole.setCrtUser(userId);
            zzCodeMeetingRole.setCrtName(userName);
            zzCodeMeetingRole.setCrtHost(userIp);
            String meetId = this.zzCodeMeetingRoleService.add(zzCodeMeetingRole);
            res.data(meetId);
            String duplicate = "0";
            if(duplicate.equals(meetId)){
                res.data("名称或者代码重复");
                res.rel(false);
            }
        }catch (Exception e){
            logger.error(common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
        }
        return  res;
    }
    /**
     * @MethodName: update
     * @Description: 修改
     * @Param: [ZzCodeMeetingRole]
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/20
     **/
    @PutMapping("update")
    public  ObjectRestResponse update(@RequestBody ZzCodeMeetingRole zzCodeMeetingRole) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        String userId = common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        String userNo = common.nulToEmptyString(request.getHeader("dnname"));
        String userIp = common.nulToEmptyString(request.getHeader("userHost"));
        try {
            zzCodeMeetingRole.setUpdHost(userIp);
            zzCodeMeetingRole.setUpdName(userName);
            zzCodeMeetingRole.setUpdUser(userId);
            this.zzCodeMeetingRoleService.update(zzCodeMeetingRole);
        }catch (Exception e){
            res.rel(false);
            res.data("系统出错");
            logger.error(common.getExceptionMessage(e));
        }
        return res;
    }
    /**
     * @MethodName: delete
     * @Description: 删除
     * @Param: [param]code 编码，id主键 两个任意传一个即可
     * @Return: com.github.hollykunge.security.common.msg.ObjectRestResponse
     * @Author: zhuqz
     * @Date: 2019/9/23
     **/
    @DeleteMapping("delete")
    public ObjectRestResponse delete(@RequestBody Map param) throws Exception{
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        int noExist = 0;
        int noParam = -2;
        try {
            int i = this.zzCodeMeetingRoleService.deleteData(param);
            if(i==noExist){
                res.rel(false);
                res.data("主键或者编码不存在");
            }else if(i==noParam){
                res.rel(false);
                res.data("请传递要删除的主键或者编码");
            }
        }catch (Exception e){
            res.rel(false);
            res.data("系统出错");
            logger.error(common.getExceptionMessage(e));
        }
        return res;
    }
    /**
     * @MethodName: query
     * @Description: 查询
     * @Param: [param] pageSize、pageNo页码页数；isUse是否使用1是0否；name名称
     * @Return: com.github.hollykunge.security.common.msg.TableResultResponse
     * @Author: zhuqz
     * @Date: 2019/9/23
     **/
    @GetMapping("query")
    public TableResultResponse query(@RequestParam Map param) throws Exception{
        return this.zzCodeMeetingRoleService.query(param);
    }
}
