package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.admin.feign.hystrix.RandomSelectDefaultAvatorHystrix;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:09 2019/8/29
 */
@FeignClient(value = "service-dfsfile",fallbackFactory = RandomSelectDefaultAvatorHystrix.class)
public interface RandomSelectDefaultAvatorFeign {
    @RequestMapping(value = "/fdfs/file/randomSelectDefaultAvator",method = RequestMethod.GET)
    ObjectRestResponse<FileInfoVO> randomSelectDefaultAvator ();
}
