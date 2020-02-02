package com.github.hollykunge.security.common.msg.auth;

import com.github.hollykunge.security.common.constant.RestCodeConstants;
import com.github.hollykunge.security.common.msg.BaseResponse;

/**
 * token错误返回类型
 * @author 协同设计小组
 * @date 2017/8/23
 */
public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
