package com.github.hollykunge.security.common.rest;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.context.BaseContextHandler;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-15 8:48
 */
@Slf4j
public class BaseController<Biz extends BaseBiz, Entity> {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected Biz baseBiz;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Entity> add(@RequestBody Entity entity) {
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<Entity>().rel(true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Entity> get(@PathVariable String id) {
        ObjectRestResponse<Entity> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.selectById(id);
        entityObjectRestResponse.data((Entity) o).rel(true);
        return entityObjectRestResponse;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Entity> update(@RequestBody @Valid Entity entity) {
        baseBiz.updateSelectiveById(entity);
        return new ObjectRestResponse<Entity>().rel(true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<Entity> remove(@PathVariable String id) {
        Object deletingObject = baseBiz.selectById(id);
        baseBiz.deleteById(id);
        return new ObjectRestResponse<Entity>().rel(true).data(deletingObject);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Entity>> all() {
        List list = baseBiz.selectListAll();
        return new ListRestResponse("",list.size(),list);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Entity> page(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        return baseBiz.selectByQuery(query);
    }

    public String getCurrentUserName() {
        return BaseContextHandler.getUsername();
    }

    public String getCurrentUserId() {
        return BaseContextHandler.getUserID();
    }
}
