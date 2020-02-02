package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.Like;
import com.github.hollykunge.security.service.LikeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yzq
 * @Date: 创建于 2019/7/2 10:29
 */
@RestController
@RequestMapping("like")
public class LikeController extends BaseController<LikeService,Like> {
}
