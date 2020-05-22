package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.RandomSelectDefaultAvatorFeign;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:55 2020/5/18
 */
@Component
public class RandomSelectDefaultAvatorHystrix extends BaseHystrixFactory<RandomSelectDefaultAvatorHystrix> implements RandomSelectDefaultAvatorFeign {
    @Override
    public ObjectRestResponse<FileInfoVO> randomSelectDefaultAvator() {
        return getHystrixObjectReponse();
    }
}
