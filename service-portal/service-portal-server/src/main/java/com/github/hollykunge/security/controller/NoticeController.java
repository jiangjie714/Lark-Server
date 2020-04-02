package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.entity.Notice;
import com.github.hollykunge.security.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description: 工作台-公告
 * @author: dd
 * @since: 2019-06-08
 */
@RestController
@RequestMapping("notice")
public class NoticeController extends BaseController<NoticeService, Notice>{

    @Autowired
    NoticeService noticeService;
    /**
     * fansq
     * 20-1-3
     * 添加门户公告数据获取 接口
     * @param orgCode
     * @return
     */
    @RequestMapping(value = "/orgNotice",method = RequestMethod.GET)
    public ListRestResponse<List<Notice>> findUserNotic(@RequestParam("orgCode") String orgCode){
        String userSecretLevel = request.getHeader("userSecretLevel");
        if(StringUtils.isEmpty(userSecretLevel)){
            throw new BaseException("该用户无密级...");
        }
        List<Notice> notices = baseBiz.selectNoticList(orgCode);
        List<Notice> noticeList= baseBiz.selectNotic(orgCode,userSecretLevel);
        notices.addAll(noticeList);
        notices = notices.stream().sorted(Comparator.comparing(Notice::getSendTime).reversed()).collect(Collectors.toList());
        return new ListRestResponse<>("查询成功！",notices.size(),notices);
    }
    /**
     * todo:使用
     * @param orgCode
     * @return
     */
    @RequestMapping(value = "/myself", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Notice>> orgNotices(@RequestParam String orgCode) {
        String userSecretLevel = request.getHeader("userSecretLevel");
        if(StringUtils.isEmpty(userSecretLevel)){
            throw new BaseException("该用户无密级...");
        }
        Notice notice = new Notice();
        notice.setOrgCode(orgCode);
        List<Notice> notices = baseBiz.selectList(notice,userSecretLevel);
        return new ListRestResponse("",notices.size(),notices);
    }

    /**
     * todo:使用
     * @param params
     * @return
     */
    @Override
    @RequestMapping(value = "/myself/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Notice> page(@RequestParam Map<String, Object> params) {
        String orgCode = (String) params.get("orgCode");
        if(StringUtils.isEmpty(orgCode)){
            throw new BaseException("该用户无组织...");
        }
        String userSecretLevel = request.getHeader("userSecretLevel");
        if(StringUtils.isEmpty(userSecretLevel)){
            throw new BaseException("该用户无密级...");
        }
        Query query = new Query(params);
        return baseBiz.page(query,userSecretLevel,orgCode);
    }
}
