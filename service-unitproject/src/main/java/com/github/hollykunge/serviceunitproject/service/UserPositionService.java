package com.github.hollykunge.serviceunitproject.service;

import org.springframework.context.annotation.Bean;
import com.github.hollykunge.serviceunitproject.common.UserInfo;

public interface UserPositionService {
    public UserInfo getUserPosition(String userID);
}
