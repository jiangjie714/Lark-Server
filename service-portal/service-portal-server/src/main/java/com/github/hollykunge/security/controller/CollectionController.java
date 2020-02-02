package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.Collection;
import com.github.hollykunge.security.service.CollectionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 工作台-我的收藏
 * @author: dd
 * @since: 2019-06-08
 */
@RestController
@RequestMapping("collection")
public class CollectionController extends BaseController<CollectionService, Collection> {

    /**
     * 获取最后5条数据
     * @param id 用户id
     * @return
     */
//    @GetMapping("/{id}")
//    public ListRestResponse<Collection> getUserCollections(@PathVariable String id){
//        List<Collection> collectionList = baseBiz.selectLastFive(id);
//        return new ListRestResponse("", collectionList.size(), collectionList);
//    }
}
