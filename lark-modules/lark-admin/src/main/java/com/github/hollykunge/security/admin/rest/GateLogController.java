package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.common.context.BaseContextHandler;
import com.github.hollykunge.security.common.util.ExportExcelUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.admin.biz.GateLogBiz;
import com.github.hollykunge.security.admin.entity.GateLog;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.hollykunge.security.admin.constant.AdminCommonConstant.*;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-07-01 20:32
 */
@Slf4j
@Controller
@RequestMapping("gateLog")
public class GateLogController extends BaseController<GateLogBiz, GateLog> {
    /**
     * todo:使用
     * 分页获取数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public TableResultResponse<GateLog> page(@RequestParam Map<String, Object> params) {

        String pid = request.getHeader("pid");

        if (StringUtils.isEmpty(pid)) {
            pid = BaseContextHandler.getUsername();
        }
        return baseBiz.pageByRole(new Query(params),pid);
    }

    /**
     * todo:使用
     * 导出日志
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void download(HttpServletResponse response) throws Exception {
        String pid = null;
        pid = request.getHeader("pid");
        if (StringUtils.isEmpty(pid)) {
            pid = BaseContextHandler.getUsername();
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("日志文件.xls", "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
//        List<GateLog> gateLogs = baseBiz.selectListAll();
        List<GateLog> gateLogs = baseBiz.gateLogExport();
        List<GateLog> gateLogsTemp = new ArrayList<>();

        if ("4".equals(pid)) {
            gateLogsTemp = gateLogs.stream().filter(gateLog -> "2".equals(gateLog.getPid()) || "3".equals(gateLog.getPid())).collect(Collectors.toList());
        } else if ("3".equals(pid)) {
            gateLogsTemp = gateLogs.stream().filter(gateLog -> !"2".equals(gateLog.getPid()) && !"3".equals(gateLog.getPid())).collect(Collectors.toList());
        } else {
            gateLogsTemp = gateLogs;
        }

        String columName = "菜单,功能,主机ip,是否成功,访问路径,访问时间,访问人员姓名,身份证号";
        String columCode = "menu,opt,crtHost,isSuccess,uri,crtTime,crtName,pid";
        String sheetName = "日志详情";
        byte[] export = ExportExcelUtils.export(gateLogsTemp, columName, columCode, sheetName, outputStream);
        IOUtils.write(export, outputStream);
    }
}
