package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkTaskTagbiz;
import com.github.hollykunge.security.task.entity.LarkTaskTag;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskTag.js
 */
@RestController
@RequestMapping(value = "/taskTag")
public class TaskTagController extends BaseController<LarkTaskTagbiz,LarkTaskTag> {


    /**
     * 新增
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/task_tag/save', data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public BaseResponse save(@RequestBody LarkTaskTag larkTaskTag){
        larkTaskTag.setId(UUIDUtils.generateShortUuid());
        baseBiz.insertSelective(larkTaskTag);
        return new BaseResponse(200,"新增成功！");
    }


}
