package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.message.ZzAt;
import com.workhub.z.servicechat.service.ZzAtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 提及（@）功能实现(ZzAt)表控制层
 *
 * @author 忠
 * @since 2019-05-10 14:22:34
 */
@RestController
@RequestMapping("/zzAt")
public class ZzAtController{
    private static Logger log = LoggerFactory.getLogger(ZzAtController.class);
    /**
     * 服务对象
     */
    @Resource
    private ZzAtService zzAtService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public ObjectRestResponse<ZzAt> selectOne(@RequestParam("id") String id) {
        return new ObjectRestResponse().data(this.zzAtService.queryById(id)).msg("200").rel(true);
    }

    @PostMapping("/create")
    public ObjectRestResponse insert(@RequestBody ZzAt zzAt){
        zzAt.setId(RandomId.getUUID());
        try {
            Common.putEntityNullToEmptyString(zzAt);
        }catch (Exception e){
            log.error(Common.getExceptionMessage(e));
        }

        this.zzAtService.insert(zzAt);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
//        if (insert == 0){
//            objectRestResponse.data("失败");
//            return objectRestResponse;
//        }
        objectRestResponse.data("成功");
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        return objectRestResponse;
    }

    @PostMapping("/update")
    public ObjectRestResponse update(@RequestBody ZzAt zzAt, @RequestParam("token")String token){
        Integer update = this.zzAtService.update(zzAt);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        if (update == 0){
            objectRestResponse.data("操作失败");
            return objectRestResponse;
        }
        objectRestResponse.data("成功");
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        return objectRestResponse;
    }

    @DeleteMapping("/delete")
    public ObjectRestResponse delete(@RequestParam("id")String id){
        boolean flag = this.zzAtService.deleteById(id);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.data("成功");
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        return objectRestResponse;
    }

    /**
     * 查询用户标记消息
     * @param receiveId 接收人id；groupId 群id；
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误；数据列表
     * @author zhuqz
     * @since 2019-06-14
     */
    @GetMapping ("/getList")
    public TableResultResponse<ZzAt> getList(
                                             @RequestParam("receiveId")String receiveId,
                                             @RequestParam("groupId")String groupId,
                                             @RequestParam(value = "page",defaultValue = "1")Integer page,
                                             @RequestParam(value = "size",defaultValue = "10")Integer size){

        TableResultResponse dataList = null;
        try {
            dataList = this.zzAtService.getList(receiveId,groupId,page,size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}