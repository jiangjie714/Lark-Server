package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.constants.Constants;
import com.github.hollykunge.security.entity.Feedback;
import com.github.hollykunge.security.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * 问题反馈
 *
 * @author hollykunge
 */
@RestController
@RequestMapping("feedback")
public class FeedbackController extends BaseController<FeedbackService, Feedback> {
    /**
     * todo:未使用
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public TableResultResponse<Feedback> page(@RequestParam Map<String, Object> params) {
        String roleId = (String) params.get("roleId");
        if (!Objects.equals(Constants.ROLE_ID, roleId)) {
            String userId = request.getHeader("userId");
            params.put("crtUser", userId);
        }
        params.remove("roleId");
        //查询列表数据
        Query query = new Query(params);
        return baseBiz.selectByQuery(query);
    }
}
