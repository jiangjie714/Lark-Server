package com.workhub.z.servicechat.job;

import com.workhub.z.servicechat.VO.RequireApproveAuthorityVo;
import com.workhub.z.servicechat.config.AnswerToFrontReponse;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.config.systemMessage;
import com.workhub.z.servicechat.entity.ZzRequireApproveAuthority;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.server.Const;
import com.workhub.z.servicechat.service.ZzRequireApproveAuthorityService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author:zhuqz
 * description: 新建群、会议、附件审批是否需要权限
 * date:2019/11/11 13:55
 **/
public class RequireApproveAuthorityTask extends QuartzJobBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    ZzRequireApproveAuthorityService zzRequireApproveAuthorityService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        try {
            ZzRequireApproveAuthority zzRequireApproveAuthority = zzRequireApproveAuthorityService.queryData();
            //如果查询不用审批的权限存在
            if(zzRequireApproveAuthority!=null
                    && zzRequireApproveAuthority.getSocketTeam() != null
                    && !"".equals(zzRequireApproveAuthority.getSocketTeam())){
                String[] teamArr = zzRequireApproveAuthority.getSocketTeam().split(",");
                List teamList  = Arrays.asList(teamArr);
                //如果不用审批权限里，包含了整个系统,直接通知 不用审批权限了
                if(teamList.contains(Const.GROUP_SYS)){
                    AnswerToFrontReponse answerToFrontReponse = new AnswerToFrontReponse();
                    answerToFrontReponse.setCode(MessageType.APPROVE_AUTHORITY_CODE);
                    RequireApproveAuthorityVo requireApproveAuthorityVo = new RequireApproveAuthorityVo();
                    requireApproveAuthorityVo.setRequireApproveAuthority(MessageType.NO_REQUIRE_APPROVE_AUTHORITY+"");
                    answerToFrontReponse.setData(requireApproveAuthorityVo);
                    systemMessage.sendMessageToFront(Const.GROUP_SYS,answerToFrontReponse,rabbitMqMsgProducer);
                }else{
                    //1先发送全系统，告知需要审批权限，防止出现权限收不回来问题
                    //如果想前端可以在修改数据配置后，可以自动收回权限，去掉注释,否则想收回不需要审批的权限，只能重启客户端
                    AnswerToFrontReponse answerAll = new AnswerToFrontReponse();
                    answerAll.setCode(MessageType.APPROVE_AUTHORITY_CODE);
                    RequireApproveAuthorityVo requireApproveAuthorityVo = new RequireApproveAuthorityVo();
                    //需要审批权限
                    requireApproveAuthorityVo.setRequireApproveAuthority(MessageType.REQUIRE_APPROVE_AUTHORITY+"");
                    answerAll.setData(requireApproveAuthorityVo);
                    systemMessage.sendMessageToFront(Const.GROUP_SYS,answerAll,rabbitMqMsgProducer);
                    //2再发送不用审批权限的通知
                    for(int i=0;i<teamList.size();i++){
                        String orgCode = teamList.get(i).toString();
                        AnswerToFrontReponse answerOrg = new AnswerToFrontReponse();
                        answerOrg.setCode(MessageType.APPROVE_AUTHORITY_CODE);
                        RequireApproveAuthorityVo orgVo = new RequireApproveAuthorityVo();
                        //不同审批权限
                        orgVo.setRequireApproveAuthority(MessageType.NO_REQUIRE_APPROVE_AUTHORITY+"");
                        answerOrg.setData(orgVo);
                        systemMessage.sendMessageToFront(orgCode,answerOrg,rabbitMqMsgProducer);
                }
        }
    }else {
               //发送通知，需要审批权限
                AnswerToFrontReponse answerToFrontReponse = new AnswerToFrontReponse();
                answerToFrontReponse.setCode(MessageType.APPROVE_AUTHORITY_CODE);
                RequireApproveAuthorityVo requireApproveAuthorityVo = new RequireApproveAuthorityVo();
                requireApproveAuthorityVo.setRequireApproveAuthority(MessageType.REQUIRE_APPROVE_AUTHORITY+"");
                answerToFrontReponse.setData(requireApproveAuthorityVo);
                systemMessage.sendMessageToFront(Const.GROUP_SYS,answerToFrontReponse,rabbitMqMsgProducer);
            }
        } catch (Exception e) {
            logger.error("定时任务刷新不需要审批权限出错！！！");
            logger.error(common.getExceptionMessage(e));
        }
    }
}
