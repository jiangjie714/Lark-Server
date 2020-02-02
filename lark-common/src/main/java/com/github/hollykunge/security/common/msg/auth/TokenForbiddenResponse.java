package com.github.hollykunge.security.common.msg.auth;

import com.github.hollykunge.security.common.constant.RestCodeConstants;
import com.github.hollykunge.security.common.msg.BaseResponse;

/**
 * token禁用返回类型
 * @author 协同设计小组
 * @date 2017/8/25
 */
public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
