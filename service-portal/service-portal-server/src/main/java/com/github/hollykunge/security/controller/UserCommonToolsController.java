package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.entity.CommonTools;
import com.github.hollykunge.security.entity.UserCommonTools;
import com.github.hollykunge.security.service.CommonToolsService;
import com.github.hollykunge.security.service.UserCommonToolsService;
import com.github.hollykunge.security.vo.UserCommonToolsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户设置常用工具类接口
 * @author zhhongyu
 * @since 2019-06-19
 */
@RestController
@RequestMapping("tool")
public class UserCommonToolsController extends BaseController<UserCommonToolsService, UserCommonTools> {
    @Autowired
    private CommonToolsService commonToolsService;
    /**
     * todo:使用
     * 获取所有的常用工具列表，如果该用户设置该常用工具为显示，
     * defaultCheck字段为true，否则为false
     * @return
     */
    @RequestMapping(value = "/myAll", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<UserCommonToolsVO> allCommonTools(@RequestParam Map<String, Object> params) {
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        //查询列表数据
        Query query = new Query(params);
        TableResultResponse<UserCommonToolsVO> tableResult = baseBiz.userCommonToolsTable(query, userID);
        return tableResult;
    }

    /**
     * todo:使用
     * 用户增加常用工具
     * @param userCommonTools 用户常用工具实体类，toolId必传
     * @return
     */
    @Override
    @RequestMapping(value = "/myself", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<UserCommonTools> add(@RequestBody UserCommonTools userCommonTools) {
        if(StringUtils.isEmpty(userCommonTools.getToolId())){
            throw new BaseException("commonToolId is null..");
        }
        String userID = request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        userCommonTools.setUserId(userID);
        if(baseBiz.selectCount(userCommonTools) > 0){
            return new ObjectRestResponse<>().rel(false).msg("fail，user exist this commonTool...");
        }
        return super.add(userCommonTools).rel(true).msg("success...").rel(true);
    }

    /**
     * todo:使用
     * 用户移除常用工具类
     * @param commonToolId
     * @return
     */
    @RequestMapping(value = "/myself", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<CommonTools> removeCommontools(@RequestParam String commonToolId) {
        if(StringUtils.isEmpty(commonToolId)){
            throw new BaseException("commonToolId is null");
        }
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        UserCommonTools userCommonTools= new UserCommonTools();
        userCommonTools.setUserId(userID);
        userCommonTools.setToolId(commonToolId);
        if(baseBiz.selectCount(userCommonTools) == 0){
            return new ObjectRestResponse<>().msg("fail，user don't have this commonTool......");
        }
        CommonTools commonTools = commonToolsService.selectById(commonToolId);
        baseBiz.delete(userCommonTools);
        return new ObjectRestResponse<CommonTools>().msg("success..").rel(true).data(commonTools);
    }

    /**
     * todo:使用
     * 用户获取显示常用工具类列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/myself", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<UserCommonToolsVO>> userCards(HttpServletRequest request) {
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        List<UserCommonToolsVO> userCardVOS = baseBiz.userCommonTools(userID);
        return new ListRestResponse("",userCardVOS.size(),userCardVOS);
    }

    /**
     * fansq
     * 19-12-17
     * 根据用户id和常用链接id查询指定链接是否是常用
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse findCommonTools(@RequestParam String commonToolsId){
        if(StringUtils.isEmpty(commonToolsId)){
            throw new BaseException("commonToolId is null");
        }
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        UserCommonTools userCommonTools= new UserCommonTools();
        userCommonTools.setUserId(userID);
        userCommonTools.setToolId(commonToolsId);
        BaseResponse base = new BaseResponse();
        if(baseBiz.selectCount(userCommonTools) > 0){
            return base;
        }
        base.setStatus(404);
        base.setMessage("该链接已不是常用链接");
        return base;
    }
}
