package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.biz.LarkTaskFileBiz;
import com.github.hollykunge.security.task.constant.TaskCommon;
import com.github.hollykunge.security.task.entity.LarkFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应 file.js
 */
@RestController
@RequestMapping(value = "/file")
public class FileController extends BaseController<LarkTaskFileBiz, LarkFile> {

    @Autowired
    private LarkTaskFileBiz larkTaskFilebiz;

    /**
     * 上传task任务关联文件
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<LarkFile> taskFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("taskCode") String taskCode,
            @RequestParam("projectCode") String projectCode){
        String userId = request.getHeader("userId");
       return  larkTaskFilebiz.taskFileUpload(file,taskCode,projectCode,userId);
    }

    /**
     * 下载task关联文件
     * @return
     */
    @RequestMapping(value = "download",method = RequestMethod.GET)
    public void taskFileDownload(@RequestParam("fileId") String fileId, HttpServletResponse response) throws IOException{
        larkTaskFilebiz.taskFileDownload(fileId,response);
    }

    /**
     * @deprecated 移动到文件夹获取了
     * 文件列表
     * @param {}} data
     *  export function list(data) {
     *    return $http.get('project/file', data);
     *  }
     */
    @RequestMapping(value = "/taskFileList",method = RequestMethod.GET)
    public TableResultResponse taskFileList(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        return larkTaskFilebiz.taskFileList(query);
    }

    /**
     * 文件预览
     * @param {*} fileCode 文件id
     *  export function read(fileCode) {
     *   return $http.get('project/file/read', {fileCode: fileCode});
     *   }
     */
    @RequestMapping(value = "/read",method = RequestMethod.GET)
    public void read() throws IOException {
        //暂时这么写  返回给前端文件内容
        //fileService.read(fileCode,response);
    }

    /**
     * 文件信息编辑
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/file/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public BaseResponse edit(@RequestBody Object data){
        //todo 暂时这么写
        return new BaseResponse(200,"文件信息编辑成功");
    }

    /**
     * 移到回收站
     * @param {*} fileCode
     *   export function recycle(fileCode) {
     *       return $http.get('project/file/recycle', {fileCode: fileCode});
     *   }
     */
    @RequestMapping(value = "/recycle",method = RequestMethod.PUT)
    public ObjectRestResponse<Object> recycle(@RequestParam("fileCode") String fileCode){
        LarkFile larkFile = new LarkFile();
        larkFile.setId(fileCode);
        larkFile.setDeleted(TaskCommon.NUMBER_ONE_STRING);
        larkFile.setDeletedTime(new Date());
        baseBiz.updateSelectiveById(larkFile);
        LarkFile larkFile1 = baseBiz.selectById(fileCode);
        larkFile1.setDeleted(TaskCommon.NUMBER_ONE_STRING);
        return new ObjectRestResponse<>().data(larkFile1).rel(true);
    }

    /**
     * 还原
     * @param {*} fileCode
     *   export function recovery(fileCode) {
     *       return $http.get('project/file/recovery', {fileCode: fileCode});
     *   }
     */
    @RequestMapping(value = "/recovery",method = RequestMethod.PUT)
    public ObjectRestResponse<Object> recovery(@RequestParam("fileCode") String fileCode){
        LarkFile larkFile = new LarkFile();
        larkFile.setId(fileCode);
        larkFile.setDeleted("0");
        baseBiz.updateSelectiveById(larkFile);
        LarkFile larkFile1 = baseBiz.selectById(fileCode);
        larkFile1.setDeleted("0");
        return new ObjectRestResponse<>().data(larkFile1).rel(true);
    }

    /**
     * 删除 取消关联
     * @param {*} fileCode
     *   export function del(fileCode) {
     *       return $http.delete('project/file/delete', {fileCode: fileCode});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ObjectRestResponse<Object> delete(@RequestParam("fileCode")  String fileCode){
        LarkFile larkFile = baseBiz.selectById(fileCode);
        baseBiz.deleteById(fileCode);
        return new ObjectRestResponse<>().msg("删除成功！").data(larkFile).rel(true);
    }

}
