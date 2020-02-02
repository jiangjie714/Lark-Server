package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.Message;
import com.github.hollykunge.security.service.MessageService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 工作台-消息
 * @author: dd
 * @since: 2019-06-08
 */
@RestController
@RequestMapping("message")
public class MessageController extends BaseController<MessageService, Message> {

    @RequestMapping(value = "/userMessage", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Message>> userMessage() {
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        Message message = new Message();
        message.setUserId(userID);
        List<Message> messages = baseBiz.selectList(message);
        return new ListRestResponse("",messages.size(),messages);
    }
}
