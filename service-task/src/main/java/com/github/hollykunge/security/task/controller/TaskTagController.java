package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkTaskTagBiz;
import com.github.hollykunge.security.task.biz.LarkTaskToTagBiz;
import com.github.hollykunge.security.task.entity.LarkTaskTag;
import com.github.hollykunge.security.task.entity.LarkTaskToTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskTag.js
 */
@RestController
@RequestMapping(value = "/taskTag")
public class TaskTagController extends BaseController<LarkTaskTagBiz,LarkTaskTag> {

    @Autowired
    private LarkTaskToTagBiz larkTaskToTagBiz;

    /**
     * @deprecated  todo 没用了
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

    /**
     * @author fansq
     * @deprecation 标签页面 数据显示接口拆分 第一步
     * @return 标签列表 不分页
     */
    @RequestMapping(value = "/getAllTag",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<LarkTaskTag>> getAllTag(@RequestParam("projectCode")String projectCode){
        Example example = new Example(LarkTaskTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectCode",projectCode);
        List<LarkTaskTag> taskTags = baseBiz.selectByExample(example);
        return new ObjectRestResponse<>().data(taskTags).rel(true);
    }

    @RequestMapping(value = "/disassociateTaskForTag",method = RequestMethod.DELETE)
    public ObjectRestResponse<LarkTaskToTag> disassociateTaskForTag(@RequestBody LarkTaskToTag larkTaskToTag){
        larkTaskToTagBiz.delete(larkTaskToTag);
        return new ObjectRestResponse<>().rel(true);
    }
}
