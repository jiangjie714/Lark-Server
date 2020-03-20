package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.biz.LoginStateBiz;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 登录统计接口
 * @since: Create in 17:11 2020/3/19
 */
@Controller
@RequestMapping("loginstate")
public class LoginStateController {
    @Autowired
    private LoginStateBiz loginStateBiz;

    @RequestMapping(value = "num",method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Map<String,Long>>> orgLoginNum(@RequestParam String orgCode) throws Exception {
        List<Map<String, Long>> maps = loginStateBiz.orgLoginNum(orgCode);
        return new ListRestResponse<List<Map<String,Long>>>("",maps.size(),maps);
    }
    @RequestMapping(value = "times",method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Map<String,Long>>> orgLoginTimes(@RequestParam String orgCode) throws Exception {
        List<Map<String, Long>> maps = loginStateBiz.orgLoginTimes(orgCode);
        return new ListRestResponse<List<Map<String,Long>>>("",maps.size(),maps);
    }
}
