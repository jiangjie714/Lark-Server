package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.PrivateFileVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzPrivateMsgDao;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 私人消息(ZzPrivateMsg)表服务实现类
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
@Service("zzPrivateMsgService")
public class ZzPrivateMsgServiceImpl implements ZzPrivateMsgService {
    private static Logger log = LoggerFactory.getLogger(ZzPrivateMsgServiceImpl.class);
    @Resource
    private ZzPrivateMsgDao zzPrivateMsgDao;
    @Override
    //query 查询附件名称
    public TableResultResponse<PrivateFileVo> getFileList(String userId, String receiverId, String query, int page, int size) throws Exception {
        PageHelper.startPage(page, size);
        List<PrivateFileVo> dataList =this.zzPrivateMsgDao.getFileList(userId,receiverId,query);
        //null的String类型属性转换空字符串
        Common.putVoNullStringToEmptyString(dataList);
        PageInfo<PrivateFileVo> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<PrivateFileVo> res = new TableResultResponse<PrivateFileVo>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }
}