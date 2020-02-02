package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.Notice;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NoticeMapper extends Mapper<Notice> {

    /**
     * 通过userId查询前六条数据
     * @param userId
     * @return
     */
    List<Notice> getNoticeByUserIdTopSix(@Param("userId") String userId);
}