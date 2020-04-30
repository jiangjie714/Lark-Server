package com.github.hollykunge.security.simulation.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.simulation.biz.AssembleResultBiz;
import com.github.hollykunge.security.simulation.biz.MongoResultBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.vo.SystemInfoVo;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.hollykunge.security.simulation.config.Constant.NAME_REGULATION_WRONG;

/**
 * @author jihang
 */

@RestController
@RequestMapping("/mongoResult")
public class MongoResultController extends BaseController<MongoResultBiz, SystemInfo> {

    @Autowired
    private AssembleResultBiz assembleResultBiz;

    /**
     * 增加
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse create(@RequestBody SystemInfo entity) throws Exception {
        int ret = baseBiz.create(entity);
        switch (ret) {
            case NAME_REGULATION_WRONG:
                return new ObjectRestResponse().rel(false).msg("名称不符合规范");
            default:
                return new ObjectRestResponse().rel(true).msg("");
        }
    }

    /**
     * 查询
     */
    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse retrieve(@RequestParam("systemId") String systemId) {
        String ret = baseBiz.retrieve(systemId);
        return new ObjectRestResponse().rel(true).data(ret);
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse update(@RequestBody SystemInfoVo entity) {
        if(null == baseBiz.update(entity)) {
            return new ObjectRestResponse().rel(false).msg("更新失败");
        }
        return new ObjectRestResponse().rel(true).msg("");
    }

    /**
     * 配置文件生成及下载
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.POST)
    @ResponseBody
    public void getConfig(@RequestBody SystemInfoVo entity, HttpServletResponse response) {
        try {
            org.jdom2.Document rootDocument = baseBiz.update(entity);
            XMLOutputter XMLOut = new XMLOutputter(assembleResultBiz.FormatXML());
            XMLOut.output(rootDocument, response.getOutputStream());
        } catch (IOException ignored) {
        }
    }
}
