package com.github.hollykunge.security.simulation.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.simulation.biz.HistoryBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.vo.HistoryInfoVo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController extends BaseController<HistoryBiz, SystemInfo> {

    @RequestMapping(value = "/systemHistory", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<String>> systemHistory(@RequestParam("systemId") String systemId) {
        List<String> histories = new ArrayList<>();
        try {
            histories = baseBiz.systemHistory(systemId);
        } catch (Exception e) {
            // TODO
        }
        return new ListRestResponse("", histories.size(), histories);
    }

    @RequestMapping(value = "/oneHistoryInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse oneHistoryInfo(@RequestParam("name") String name) {
        HistoryInfoVo hi;
        try {
            hi = baseBiz.oneHistoryInfo(name);
        } catch (Exception e) {
            return new ObjectRestResponse().rel(false);
        }
        return new ObjectRestResponse().rel(true).data(hi);
    }

    @RequestMapping(value = "/deleteOneHistory", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse deleteOneHistory(@RequestParam("historyName") String historyName) {
        boolean ret = baseBiz.deleteOneHistory(historyName);
        return new ObjectRestResponse().rel(ret).msg("");
    }

    @RequestMapping(value = "/downloadTopicData", method = RequestMethod.POST)
    @ResponseBody
    public void downloadTopicData(
            @RequestBody HistoryInfoVo entity,
            HttpServletResponse response) throws Exception {

        byte[] content = baseBiz.downloadTopicData(
                entity.getName(), entity.getTopic(), entity.getIsStruct());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(entity.getFileName(), "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(content, outputStream);
    }
}
