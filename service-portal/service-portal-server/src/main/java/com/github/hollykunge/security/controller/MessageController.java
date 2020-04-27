package com.github.hollykunge.security.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.Message;
import com.github.hollykunge.security.service.MessageService;
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
        String userId =  request.getHeader("userId");
        Message message = new Message();
        message.setUserId(userId);
        List<Message> messages = baseBiz.selectList(message);
        return new ListRestResponse("",messages.size(),messages);
    }
}
