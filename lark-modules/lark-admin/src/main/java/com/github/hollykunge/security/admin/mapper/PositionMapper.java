package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.Position;
import com.github.hollykunge.security.admin.entity.PositionDTo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author fansq
 * @since 29-8-26
 * 岗位操作mapper
 */
public interface PositionMapper extends Mapper<Position> {

    /**
     * fansq
     * @param positionDTo
     * @return 查询结果
     */
    public List<Position> selectPosition(PositionDTo positionDTo);

    /**
     * fansq
     * @param position
     * @return 删除结果
     */
    public int deletePosition(Position position);

    /**
     * fansq
     * @param position
     * @return 插入结果
     */
    public int insertPosition(Position position);

    @Select({"select count(name) from admin_position where name=#{name}"})
    public int getPositionName(String name);
    @Select({"select max(sort) from admin_position"})
    public int selectMaxSort();
    /**
     * fansq
     * @param position
     * @return 更新结果
     */
    public int updatePosition(Position position);

    /**
     * fansq
     * @param position 参数列表
     * @return 返回 查询所有或者指定岗位下的人员 结果
     */
    public List<Position> selectPositionUserMap(Position position);

    public int sortPosition(@Param("positions") List<Position> positions);
}