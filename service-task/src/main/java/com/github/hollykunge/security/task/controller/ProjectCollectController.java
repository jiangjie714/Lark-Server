package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecated 对应projectCollect.js  收藏暂时不考虑
 */
@RestController
@RequestMapping(value = "/project_collect")
public class ProjectCollectController {

    /**
     * 收藏项目
     * @param {*} code
     * @param {*} type
     *   export function collect(code, type = 'collect') {
     *    return $http.post('project/project_collect/collect', {type: type, projectCode: code});
     *   }
     */
    @RequestMapping(value = "/collect",method = RequestMethod.POST)
    public BaseResponse quit(@RequestParam("type") String type, @RequestParam("projectCode") String code){
        return new BaseResponse(200,"项目已收藏！");
    }
}
