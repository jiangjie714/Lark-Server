package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.entity.LarkFolder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkFolderMapper extends Mapper<LarkFolder> {

    /**
     * 根据taskCode 或者 ProjectCode 获取文件列表
     * @param projectCode  taskCode
     * @return
     */
    List<LarkFolder> getTaskFileList(@Param("projectCode") Object projectCode,@Param("taskCode") Object taskCode);
}