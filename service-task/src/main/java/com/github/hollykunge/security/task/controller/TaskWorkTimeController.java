package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.task.biz.LarkTaskWorkTimeBiz;
import com.github.hollykunge.security.task.entity.LarkTaskWorkTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author fansq
 * @since 20-4-24
 * @deprecation 任务工时
 */
@RestController
@RequestMapping(value = "/taskWorkTime")
public class TaskWorkTimeController extends BaseController<LarkTaskWorkTimeBiz, LarkTaskWorkTime> {

}
