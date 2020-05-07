package com.github.hollykunge.security.task.mapper;

import com.github.hollykunge.security.task.dto.LarkTaskDto;
import com.github.hollykunge.security.task.dto.TaskNum;
import com.github.hollykunge.security.task.entity.LarkTask;
import com.github.hollykunge.security.task.vo.LarkTaskVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
public interface LarkTaskMapper extends Mapper<LarkTask> {
    /**
     * 获取 我参与的 1 我创建的 2 我执行的 3 是否完成 接口 数据获取
     * @param userId 用户id
     * @param done 是否完成 默认未完成
     * @param num 1 2 3 默认我执行的 3
     * @return
     */
    List<LarkTaskDto> getTaskByUserIdAndDone(@Param("userId") String userId,@Param("done") String done,@Param("num") String num);

    /**
     * 拆分请求第二步 getTasksByProjectId
     * 根据具体任务列id  获取具体任务集合 以及任务的具体信息
     * @param larkTaskVO
     * @return
     */
    List<LarkTaskDto> getLarkTaskList(@Param("params") LarkTaskVO larkTaskVO);

    TaskNum getTotalAndComoleted(@Param("id") String id);
    String getNums(@Param("taskCode") String taskCode);

    /**
     * 标签页面 数据显示接口拆分 第二步
     * @param projectCode
     * @param taskCode
     * @return
     */
    List<LarkTaskDto> getTaskAndTag(String projectCode, String taskCode);
}