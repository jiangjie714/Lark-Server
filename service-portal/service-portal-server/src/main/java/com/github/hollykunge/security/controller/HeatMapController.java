package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.HeatMap;
import com.github.hollykunge.security.service.HeatMapService;
import com.github.hollykunge.security.vo.HeatMapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作热力图接口
 *
 * @author zhhongyu
 * @since 2019-06-19
 */
@RestController
@RequestMapping("hotmap")
public class HeatMapController extends BaseController<HeatMapService, HeatMap> {

    @Autowired
    HeatMapService heatMapService;

    /**
     * 根据userid获取工作热力图接口
     *
     * @return
     */

    @GetMapping("/allData")
    @ResponseBody
    public ListRestResponse<List<HeatMapVO>> allData() {
        String userID = request.getHeader("userId");
        if (StringUtils.isEmpty(userID)) {
            throw new BaseException("request contains no user...");
        }
        HeatMap heatMap = new HeatMap();
        heatMap.setUserId(userID);
        List<HeatMap> heatMaps = baseBiz.selectList(heatMap);

        return new ListRestResponse("", heatMapService.getHeatMap(heatMaps).size(), heatMapService.getHeatMap(heatMaps));
    }
}
