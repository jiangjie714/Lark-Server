package com.github.hollykunge.security.admin.rpc;

import com.github.hollykunge.security.admin.entity.ErrorLogEntity;
import com.github.hollykunge.security.admin.rpc.service.ErrorLogService;
import com.github.hollykunge.security.common.feign.ErrorMessageEntity;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zhhongyu
 * @description: 系统错误日志，在es没有上线之前暂时使用admin中的admin_sys_error表去落地数据
 * todo es上线后该接口，干掉
 * @since: Create in 13:42 2020/5/20
 */
@RestController
public class ErrorLogRest {
    private final ErrorLogService errorLogService;

    public ErrorLogRest(ErrorLogService errorLogService){
        this.errorLogService = errorLogService;
    }

    /**
     * 暂时将数据落地到数据库的方法
     * @param errorMessageEntity
     * @return
     */
    @RequestMapping(value = "sysError",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Boolean> saveLog(@RequestBody ErrorMessageEntity errorMessageEntity){
        ErrorLogEntity errorLogEntity = new ErrorLogEntity();
        BeanUtils.copyProperties(errorMessageEntity,errorLogEntity);
        errorLogService.insertSelective(errorLogEntity);
        return new ObjectRestResponse<>().data(true);
    }
}
