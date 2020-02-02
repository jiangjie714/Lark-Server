package com.github.hollykunge.security.admin.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fansq
 * @since 19-8-27
 * position扩展类
 */
@Data
public class PositionDTo extends Position implements Serializable {

    private int page;
    private int size;
    private List<User> users;
}
