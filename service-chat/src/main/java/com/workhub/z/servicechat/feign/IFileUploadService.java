package com.workhub.z.servicechat.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-dfsfile",fallbackFactory = FileClientFallbackFactory.class)
@Repository
public interface IFileUploadService {
    /*//设置文件文已发送状态
    //附件id，附件密级，附件所属工作类型0研讨1工具
    @RequestMapping(value = "/FileManage/fileUpdate", method = RequestMethod.POST)
    public ObjectRestResponse fileUpdate(@RequestParam("fileId") String fileId, @RequestParam("level") String level, @RequestParam("type") String type) throws Exception;*/
     //删除文件
    @RequestMapping(value = "/fdfs/file/delete", method = RequestMethod.DELETE)
    public ObjectRestResponse removeFile(@RequestParam("fileId") String fileId) throws Exception;
}
