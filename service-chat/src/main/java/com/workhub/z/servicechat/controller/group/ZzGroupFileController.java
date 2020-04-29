package com.workhub.z.servicechat.controller.group;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.FileMonitoringVo;
import com.workhub.z.servicechat.VO.GroupFileVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.group.ZzGroupFile;
import com.workhub.z.servicechat.service.ZzGroupFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

/**
 * 群文件(ZzGroupFile)表控制层
 *
 * @author 忠
 * @since 2019-05-13 10:59:08
 */
@RestController
@RequestMapping("/zzGroupFile")
public class ZzGroupFileController {

    /**
     * 服务对象
     */
    @Resource
    private ZzGroupFileService zzGroupFileService;
    private static Logger log = LoggerFactory.getLogger(ZzGroupFileController.class);
    @Autowired
    private HttpServletRequest request;
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public ObjectRestResponse selectOne(@RequestParam("id") String id) {
        ZzGroupFile entity = this.zzGroupFileService.queryById(id);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data(entity);
        return objectRestResponse;
    }

    /**
     * todo：未使用
     * 群文件查询
     * @param id 群id
     * @return
     */
    //query 查询文件名称
    @PostMapping("/groupfile")
    public TableResultResponse<GroupFileVo> groupFileList(@RequestParam("id")String id,
                                                          //@RequestParam("query")String query,
                                                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){
        String query="";//前端查询添加，文件名称，暂时没有加，这里先传个空就行
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        TableResultResponse<GroupFileVo> pageInfo = null;
        Long total = 0L;
        try {
            pageInfo = this.zzGroupFileService.groupFileList(id,userId, query, page, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        T data, int pageSize, int pageNo, int totalPage, int totalCount
        return pageInfo;
    }

    /**
     * todo:使用
     * 我上传的群文件查询
     * @param groupId
     * @return
     */
    //query 查询文件名称
    @PostMapping("/groupFileListByMe")
    public TableResultResponse<GroupFileVo> groupFileListByMe(@RequestParam("groupId")String groupId,
                                                          @RequestParam("userId")String userId,
                                                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){

        TableResultResponse<GroupFileVo> pageInfo = null;
        Long total = 0L;
        try {
            pageInfo = this.zzGroupFileService.groupFileListByMe(groupId, userId, page, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        T data, int pageSize, int pageNo, int totalPage, int totalCount
        return pageInfo;
    }

    /**
     * todo:使用
     * 群文件待审批查询
     * @param groupId
     * @return
     */
    //query 查询文件名称
    @PostMapping("/groupFileListByOwner")
    public TableResultResponse<GroupFileVo> groupFileListByOwner(@RequestParam("groupId")String groupId,
                                                          @RequestParam("userId")String userId,
                                                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){
        TableResultResponse<GroupFileVo> pageInfo = null;
        Long total = 0L;
        try {
            pageInfo = this.zzGroupFileService.groupFileListByOwner(groupId, userId, page, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        T data, int pageSize, int pageNo, int totalPage, int totalCount
        return pageInfo;
    }

    /**
     * todo:使用
     * 群文件通过审批查询
     * @param groupId
     * @return
     */
    //query 查询文件名称
    @PostMapping("/groupFileListByPass")
    public TableResultResponse<GroupFileVo> groupFileListByPass(@RequestParam("groupId")String groupId,
                                                          @RequestParam("userId")String userId,
                                                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){

        TableResultResponse<GroupFileVo> pageInfo = null;
        Long total = 0L;
        try {
            pageInfo = this.zzGroupFileService.groupFileListByPass(groupId, userId, page, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        T data, int pageSize, int pageNo, int totalPage, int totalCount
        return pageInfo;
    }

    /**
     * todo:使用
     * 文件列表查询
     *  page size ：页数页码；groupId 会议id ；userId 用户id；isGroup 文件类型： 0 私聊 1群 905 会议
     * @return
     */
    @PostMapping ("/getGroupFileList")
    public TableResultResponse<GroupFileVo> getGroupFileList(@RequestParam Map<String,String> params){
        String userId= Common.nulToEmptyString(request.getHeader("userId"));
        TableResultResponse<GroupFileVo> pageInfo = null;
        try {
            pageInfo = this.zzGroupFileService.getGroupFileList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }
    /**
     * todo:使用
     * 文件删除(删记录)
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public ObjectRestResponse delFileInfo(@RequestParam("id") String id){
        //boolean flag = this.zzGroupFileService.deleteById(id);
        //ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        //objectRestResponse.data(flag);
        this.zzGroupFileService.deleteById(id);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data("成功");
        return objectRestResponse;
    }

    /**
     * 创建
     * @param zzGroupFile
     * @return
     */
    @PostMapping("/create")
    public ObjectRestResponse insert(@RequestBody ZzGroupFile zzGroupFile){
        zzGroupFile.setFileId(RandomId.getUUID());
        zzGroupFile.setCreator("登陆人id");//TODO
        zzGroupFile.setCreateTime(new Date());
        try{
            Common.putEntityNullToEmptyString(zzGroupFile);
        }catch(Exception e){
            e.printStackTrace();
        }
//        Integer insert = this.zzGroupFileService.insert(zzGroupFile);
        this.zzGroupFileService.insert(zzGroupFile);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data("成功");
//        if (insert == null){
//            objectRestResponse.data("失败");
//            return objectRestResponse;
//        }
//        objectRestResponse.data("成功");
        return objectRestResponse;
    }

    /**
     * todo:使用
     * 修改
     * @param zzGroupFile
     * @return
     */
    @PostMapping("/update")
    public ObjectRestResponse update(@RequestBody ZzGroupFile zzGroupFile){
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        zzGroupFile.setUpdator(userId);
        zzGroupFile.setUpdateTime(new Date());
        try{
            Common.putEntityNullToEmptyString(zzGroupFile);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.zzGroupFileService.update(zzGroupFile);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data("成功");
        return objectRestResponse;
    }
    /**
     * todo:使用
     * 上传文件监控
     *参数说明：page 页码 size 每页几条 userName上传用户名称 dateBegin、dateEnd上传时间开始结束 isGroup 是否群主1是0否
     * fileName文件名称 level密级
     * @return
     */
    @PostMapping("/fileMonitoring")
    public TableResultResponse<FileMonitoringVo> fileMonitoring(@RequestParam Map<String,Object> params){
        TableResultResponse<FileMonitoringVo> pageInfo = null;
        try {
            pageInfo = this.zzGroupFileService.fileMonitoring(params);
        } catch (Exception e) {
            log.error(Common.getExceptionMessage(e));
        }
        return pageInfo;
    }
    /**
     * todo:使用
     *@Description: 会议文件上传
     *@Param: msg 上传文件信息json
     *@return:
     *@Author: zhuqz
     *@date: 2019/10/15
     */
    @PostMapping("saveFileInfByJson")
    public ObjectRestResponse saveFileInfByJson(@RequestBody String msg) throws Exception{

        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data("操作成功");
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        String userIp = Common.nulToEmptyString(request.getHeader("userHost"));
        int i = this.zzGroupFileService.saveMeetFile(msg);
        if(i!=1){
            res.rel(false);
            res.data("操作失败");
        }
        return res;
    }
}