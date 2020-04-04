package com.github.hollykunge.security.simulation.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.simulation.biz.FileResultBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.vo.BothConfigVo;
import org.springframework.web.bind.annotation.*;

import static com.github.hollykunge.security.simulation.config.Constant.NAME_REGULATION_WRONG;

/**
 * @author jihang
 */

@RestController
@RequestMapping("/fileResult")
public class FileResultController extends BaseController<FileResultBiz, SystemInfo> {

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
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse update(@RequestBody SystemInfo entity) {
        int ret = baseBiz.update(entity);
        switch (ret) {
            case NAME_REGULATION_WRONG:
                return new ObjectRestResponse().rel(false).msg("名称不符合规范");
            default:
                return new ObjectRestResponse().rel(true).msg("");
        }
    }

    /**
     * 配置文件生成及下载
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse getConfig(@RequestBody BothConfigVo entity) {
        baseBiz.getConfig(entity);
        return new ObjectRestResponse().rel(true).msg("");
    }

}
