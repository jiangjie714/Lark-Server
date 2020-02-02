package com.workhub.z.servicechat.server;

import com.alibaba.fastjson.JSONObject;
import com.workhub.z.servicechat.VO.UserOnOffLineVo;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.entity.UserInfo;
import com.workhub.z.servicechat.feign.IUserService;
import com.workhub.z.servicechat.feign.IValidateService;
import com.workhub.z.servicechat.processor.ProcessLogin;
import com.workhub.z.servicechat.processor.ProcessMsg;
import com.workhub.z.servicechat.service.ZzGroupMsgService;
import com.workhub.z.servicechat.service.ZzMeetingUserService;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class IworkWsMsgHandler implements IWsMsgHandler {
    private static Logger log = LoggerFactory.getLogger(IworkWsMsgHandler.class);
    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
    private static AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);
    @Autowired
    protected ProcessMsg processMsg;
    @Autowired
    protected ZzMeetingUserService meetingUserService;
    @Autowired
    protected ZzUserGroupService userGroupService;
    @Autowired
    protected ProcessLogin processLogin;
    @Autowired
    protected IUserService userService;
    @Autowired
    protected ZzPrivateMsgService privateMsgService;
    @Autowired
    protected ZzGroupMsgService groupMsgService;
    protected IValidateService iValidateService;
    private static IworkWsMsgHandler  serverHandler ;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        serverHandler = this;
        serverHandler.meetingUserService = this.meetingUserService;
        serverHandler.userService = this.userService;
        serverHandler.privateMsgService = this.privateMsgService;
        serverHandler.iValidateService = this.iValidateService;
        serverHandler.processMsg = this.processMsg;
        serverHandler.processLogin = this.processLogin;
        serverHandler.meetingUserService = this.meetingUserService;
        // 初使化时将已静态化的Service实例化
    }

    public static IworkWsMsgHandler me = new IworkWsMsgHandler();

    private IworkWsMsgHandler() {

    }

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     */
    @Override
//    @Cache(key = "user")
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientip = request.getClientIp();
        String token = request.getParam("token");
        String userPId = JSONObject.toJSONString(request.getParam("userPId"));
//        JSONObject userPid = JSON.parseObject(request.getParam("userPid"));
        String userid=request.getParam("userId");
//推送前端上线消息
        AnswerToFrontReponse answerToFrontReponse = new AnswerToFrontReponse();
        answerToFrontReponse.setCode(MessageType.LINESTATUS);
        UserOnOffLineVo userOnOffLineVo = new UserOnOffLineVo();
        userOnOffLineVo.setLineCode(MessageType.ONLINE+"");
        userOnOffLineVo.setUserId(userid);
        answerToFrontReponse.setData(userOnOffLineVo);
        systemMessage.sendMessageToFront(Const.GROUP_SYS,answerToFrontReponse);
//      获取用户在线信息，如在线，踢掉他
//      checkUserOnline(channelContext,userid);
//      获取用户群组信息,组织机构
        Map p2 = new HashMap<>(16);
        p2.put("userid",userid);
        UserInfo userInfo = serverHandler.userService.getUserInfo(p2);
//      加入组织
        Tio.bindGroup(channelContext, userInfo.getOrgCode());
//      用户token验证
//      iValidateService.validate(token);
//      Tio.bindToken(channelContext,token);
//      前端 参数 绑定信息
        Tio.bindBsId(channelContext,userid);
        Tio.bindUser(channelContext,userid);
//      加入系统消息组
        Tio.bindGroup(channelContext, Const.GROUP_SYS);
//       根据握手信息，将用户绑定到群组
        List<String> grouplist =  serverHandler.userGroupService.getGroupByUserId(userid);
        for (int i = 0; i < grouplist.size() ; i++) {
            String groupId  =grouplist.get(i);
            Tio.bindGroup(channelContext,groupId);
//            System.out.println("join group +"+ listGroupModel.get(i).getGroupName());
        }
//        加入会议
       List<String> meetlist =  serverHandler.meetingUserService.getMeetingByUserId(userid);
        meetlist.stream().forEach((String i) ->{
//            System.out.println(i);
            Tio.bindGroup(channelContext,i);
            });


//       IntStream.range(0,meetlist.size())
//               .forEach(i -> System.out.println(i.stream()));
       int count =  Tio.getAllChannelContexts(channelContext.getGroupContext()).size();
        System.out.println("系统当前登录"+ count + "人");
//      log.info("收到来自{}的ws握手包\r\n{}", clientip, request.toString());
        //serverHandler.processLogin.isOnline(channelContext.getBsId(),ONLINE);
        ProcessLogin.setOnLineSatus(channelContext.getBsId(), MessageType.ONLINE);
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
//        serverHandler.processLogin.offLine(channelContext.getBsId(),OFFLINE);
//        log.info("握手结束了\r\n{}", httpRequest.getClientIp(), httpRequest.toString());
    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        String userId = channelContext.getBsId();
        Tio.remove(channelContext, "receive close flag");
        //设置用户离线
        ProcessLogin.setOnLineSatus(userId,MessageType.OFFLINE);
        //推送前端离线消息
        AnswerToFrontReponse answerToFrontReponse = new AnswerToFrontReponse();
        answerToFrontReponse.setCode(MessageType.LINESTATUS);
        UserOnOffLineVo userOnOffLineVo = new UserOnOffLineVo();
        userOnOffLineVo.setLineCode(MessageType.OFFLINE+"");
        userOnOffLineVo.setUserId(userId);
        answerToFrontReponse.setData(userOnOffLineVo);
        systemMessage.sendMessageToFront(Const.GROUP_SYS,answerToFrontReponse);
        return null;
    }

    /*
     * 字符消息（binaryType = blob）过来后会走这个方法
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        HttpRequest httpRequest = wsSessionContext.getHandshakeRequest();//获取websocket握手包
        try{
            if (Objects.equals("心跳内容", text)) {
                return null;
            }
            serverHandler.processMsg.process(channelContext,text);

//        获取前端消息 展示
//        if (log.isDebugEnabled()) {
//            log.debug("握手包:{}", httpRequest);
//        }
//
//        log.info("收到ws消息:{}", text);
//        String t = String.valueOf(text);
//        JSONObject jsonObject = JSON.parseObject(text);
//        System.out.println(t);
//      String msg = channelContext.getClientNode().toString() + " 说：" + text;
//        String msg = t;
//        用tio-websocket，服务器发送到客户端的Packet都是WsResponse
//          WsResponse wsResponse = WsResponse.fromText(text, IworkServerConfig.CHARSET);
//        Tio.sendToAll(channelContext.getGroupContext(),wsResponse);
//        群发
//        Tio.bSendToGroup(channelContext.getGroupContext(), Const.GROUP_SYS, wsResponse);
        }catch (Exception e){
            log.error("发的是什么鬼东西"+text);
            return false;
        }
        //系统消息

//        Tio.sendToUser(channelContext.getGroupContext(),"123",wsResponse);
        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }
}