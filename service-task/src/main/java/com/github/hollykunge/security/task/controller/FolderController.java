package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.biz.LarkFolderBiz;
import com.github.hollykunge.security.task.biz.LarkTaskFileBiz;
import com.github.hollykunge.security.task.constant.TaskCommon;
import com.github.hollykunge.security.task.entity.LarkFile;
import com.github.hollykunge.security.task.entity.LarkFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author fansq
 * @since 20-5-11
 * @deprecation 文件夹
 */
@RestController
@RequestMapping("/folder")
public class FolderController extends BaseController<LarkFolderBiz, LarkFolder> {

    @Autowired
    private LarkTaskFileBiz larkTaskFileBiz;

    @Autowired
    private LarkFolderBiz larkFolderBiz;
    /**
     * 如果存在文件，这个文件夹不允许被删除
     * @param id
     * @return
     */
    @Override
    public ObjectRestResponse<LarkFolder> remove(String id) {
        LarkFile larkFile = new LarkFile();
        larkFile.setFolderId(id);
        if(larkTaskFileBiz.selectCount(larkFile)> TaskCommon.NUMBER_ZERO){
            throw new ClientParameterInvalid("该文件夹下存在文件，无法删除！");
        }
        return super.remove(id);
    }

    /**
     * 文件列表
     * @param {}} data
     *  export function list(data) {
     *    return $http.get('project/file', data);
     *  }
     */
    @RequestMapping(value = "/taskFileList",method = RequestMethod.GET)
    public TableResultResponse taskFileList(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        return larkFolderBiz.taskFileList(query);
    }
}
