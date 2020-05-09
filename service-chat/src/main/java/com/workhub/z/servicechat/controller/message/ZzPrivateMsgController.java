package com.workhub.z.servicechat.controller.message;

import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.PrivateFileVo;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 私人消息(ZzPrivateMsg)表控制层
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
@RestController
@RequestMapping("/zzPrivateMsg")
public class ZzPrivateMsgController{
    /**
     * 服务对象
     */
    @Resource
    private ZzPrivateMsgService zzPrivateMsgService;

    /**
     *
     * @param userId 发送人
     * @param receiver 接收人
     * @param query 文件名称
     * @param page
     * @param size
     * @return
     */
    @Decrypt
    @PostMapping("/privateFile")
    public TableResultResponse<PrivateFileVo> getFileList(@RequestParam("userId")String userId,
                                                          @RequestParam("receiver")String receiver,
                                                          @RequestParam("query")String query,
                                                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){
        TableResultResponse<PrivateFileVo> pageInfo = null;
        try {
            pageInfo = this.zzPrivateMsgService.getFileList(userId,receiver,query,page,size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }

}