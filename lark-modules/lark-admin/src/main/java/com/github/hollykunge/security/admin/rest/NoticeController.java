package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.biz.NoticeBiz;

import com.github.hollykunge.security.admin.entity.Notice;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author: yzq
 * @Date: 创建于 2019/6/4 16:37
 */

@Controller
@RequestMapping("notice")
@Api("公告模块")
public class NoticeController extends BaseController<NoticeBiz,Notice> {

    /**
     * todo:使用
     * 消息发布接口
     * @param notice 消息实体类必须带id
     * @return
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Boolean> sendNotice(@RequestBody @Valid Notice notice){
        baseBiz.sentNotice(notice);
        return new ObjectRestResponse().data(true);
    }

    /**
     * fansq
     * 取消消息发布接口
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Notice> sendCancelNotice(@PathVariable("id") String id) {
        baseBiz.sentCancelNotice(id);
        return new ObjectRestResponse().msg("取消发布成功").rel(true);
    }

    /**
     * todo:使用
     * @param params
     * @return
     */
    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Notice> page(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        String userID = request.getHeader("userId");

        return baseBiz.pageList(query,userID);
    }
}
