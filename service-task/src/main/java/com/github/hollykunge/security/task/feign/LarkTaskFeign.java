package com.github.hollykunge.security.task.feign;

import com.github.hollykunge.security.task.entity.LarkProject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author fansq
 * @since 20-5-12
 * @deprecation 调用自动更新任务进度接口  也可以使用mq
 */
@FeignClient(value = "service-task")
public interface LarkTaskFeign {

    @RequestMapping(value = "/task/autoUpdateProgress",method = RequestMethod.POST)
    LarkProject autoUpdateProgress(@RequestBody LarkProject larkProject);
}
