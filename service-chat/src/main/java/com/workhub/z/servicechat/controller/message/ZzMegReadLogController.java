package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzMegReadLog;
import com.workhub.z.servicechat.service.ZzMegReadLogService;
import org.nutz.mvc.annotation.GET;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ZzMegReadLog)表控制层
 *
 * @author makejava
 * @since 2019-09-12 17:15:24
 */
@RestController
@RequestMapping("zzMegReadLog")
public class ZzMegReadLogController {
    /**
     * 服务对象
     */
    @Resource
    private ZzMegReadLogService zzMegReadLogService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ZzMegReadLog selectOne(String id) {
        return this.zzMegReadLogService.queryById(id);
    }

    /**
     * todo:使用
     * @param pageNum
     * @param pageSize
     * @param reviserName
     * @param senderName
     * @return
     */
    @GetMapping("getAllMegRead")
    public TableResultResponse getAllMegRead(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                             @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                             @RequestParam(value = "reviserName",defaultValue ="")String reviserName,
                                             @RequestParam(value = "senderName",defaultValue = "")String senderName){
        return this.zzMegReadLogService.queryAllReadLog(pageNum,pageSize,reviserName,senderName);
    }

}