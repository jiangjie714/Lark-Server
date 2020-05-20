package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.entity.LarkFolder;
import com.github.hollykunge.security.task.mapper.LarkFolderMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkFolderBiz extends BaseBiz<LarkFolderMapper, LarkFolder> {

    @Autowired
    private LarkFolderMapper larkFolderMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 根据taskCode 或者 ProjectCode 获取文件列表
     * @param query
     * @return
     */
    public TableResultResponse<LarkFolder> taskFileList(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        Object projectCode = query.get("projectCode");
        Object taskCode = query.get("taskCode");
        List<LarkFolder> larkFiles = larkFolderMapper.getTaskFileList(projectCode,taskCode);
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkFiles);
    }
}
