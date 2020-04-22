package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.entity.LarkFile;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkFileMapper extends Mapper<LarkFile> {

    /**
     * 根据taskCode 或者 ProjectCode 获取文件列表
     * @param projectCode  taskCode
     * @return
     */
    List<LarkFile> getTaskFileList(Object projectCode,Object taskCode);
}