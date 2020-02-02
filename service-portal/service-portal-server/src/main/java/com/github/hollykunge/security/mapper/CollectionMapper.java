package com.github.hollykunge.security.mapper;

import com.github.hollykunge.security.entity.Collection;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: 工作台
 * @author: dd
 * @since: 2019-06-08
 */
public interface CollectionMapper extends Mapper<Collection> {
//    List<Collection> selectLastFiveByUserId(@Param("pId") String pId);
}
