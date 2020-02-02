package com.github.hollykunge.security.mapper;

import com.github.hollykunge.security.entity.User;
import com.github.hollykunge.security.entity.UserCard;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 用户卡片表
 * @author zhhongyu
 * @since 2019-06-11
 */
public interface UserCardMapper  extends Mapper<UserCard> {

    /**
     * fansq
     * 19-12-24
     * 操作 卡片用户关系表
     * @param userCardList
     */
    void insertUserCards(@Param("userCardList") List<UserCard> userCardList);

    /**
     * fansq
     * 19-12-24
     * 删除
     */
    void deleteUserCards(@Param("cardId") String cardId,@Param("userList") List<UserCard> userCardList);
}
