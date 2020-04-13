package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//import com.github.hollykunge.security.task.service.impl.FileService;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应 file.js
 */
@RestController
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    //private FileService fileService;
    /**
     * 文件列表
     * @param {}} data
     *  export function list(data) {
     *    return $http.get('project/file', data);
     *  }
     */
//    @RequestMapping(value = "/",method = RequestMethod.GET)
//    public TableResultResponse list(@Qualifier("") @RequestBody  Object data){
//        //new TableResultResponse(); 文件列表分页
//        // todo 暂时返回空
//        return new TableResultResponse();
//    }

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
    @RequestMapping(value = "/recycel",method = RequestMethod.GET)
    public ObjectRestResponse<Object> recycle(@RequestParam("fileCode") String fileCode){
        //return new ObjectRestResponse<Object>().data(fileService.recycle(fileCode)).msg("已移动到回收站！");
        return  null;
    }

    /**
     * 还原
     * @param {*} fileCode
     *   export function recovery(fileCode) {
     *       return $http.get('project/file/recovery', {fileCode: fileCode});
     *   }
     */
    @RequestMapping(value = "/recovery",method = RequestMethod.GET)
    public ObjectRestResponse<Object> recovery(@RequestParam("fileCode") String fileCode){
        //return new ObjectRestResponse<Object>().data(fileService.recovery(fileCode)).msg("文件以还原！");
        return null;
    }

    /**
     * 删除
     * @param {*} fileCode
     *   export function del(fileCode) {
     *       return $http.delete('project/file/delete', {fileCode: fileCode});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ObjectRestResponse<Object> delete(@RequestParam("fileCode")  String fileCode){
        return new ObjectRestResponse<>().msg("删除成功！");
    }
}
