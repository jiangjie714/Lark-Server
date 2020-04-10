package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.dao.StatisticsDao;
import com.workhub.z.servicechat.model.StatisticsChartDto;
import com.workhub.z.servicechat.model.StatisticsGroupOrgDto;
import com.workhub.z.servicechat.model.StatisticsGroupUserDto;
import com.workhub.z.servicechat.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/2/27 14:13
 * @description:统计
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    StatisticsDao statisticsDao;

    /**
     * 群统计
     * @return
     */
    @Override
    public  List<StatisticsChartDataVo> groupInfStatistics(String dateType, String orgCode){
        //获取横坐标
        List<StatisticsChartDto> statisticsXLabelVos = this.statisticsDao.getXLabel(orgCode);
        //计算每个点的值
        for(StatisticsChartDto vo:statisticsXLabelVos){
            String data = this.statisticsDao.groupInfStatistics(changeDateType(dateType),vo.getId());
            vo.setCnt(data);
        }
        return  changeChartData(statisticsXLabelVos);
    }

    /*public TableResultResponse<StatisticsGroupVo> groupStatistics(int page,int size,String groupName, String isCross) {
        PageHelper.startPage(page, size);
        List<StatisticsGroupVo> dataList =this.statisticsDao.groupStatistics(groupName,isCross);
        PageInfo<StatisticsGroupVo> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<StatisticsGroupVo> res = new TableResultResponse<StatisticsGroupVo>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }*/

    /**
     * 消息统计
     * dateType 0全部1天2本周3本月
     * @return
     */
    @Override
    public  List<StatisticsChartDataVo> msgStatistics(String dateType, String orgCode){
        //获取横坐标
        List<StatisticsChartDto> statisticsXLabelVos = this.statisticsDao.getXLabel(orgCode);
        //计算每个点的值
        for(StatisticsChartDto vo:statisticsXLabelVos){
            String data = this.statisticsDao.msgStatistics(changeDateType(dateType),vo.getId());
            vo.setCnt(data);
        }
        return  changeChartData(statisticsXLabelVos);
    }

    /*public StatisticsMsgVo msgStatistics(String dateType) {
        return this.statisticsDao.msgStatistics(dateType);
    }*/
    /**
     * 附件统计
     * @return
     * dateType 0全部1天2周3年
     */
    @Override
    public  List<StatisticsChartDataVo> fileStatistics(String dateType, String orgCode){
        //获取横坐标
        List<StatisticsChartDto> statisticsXLabelVos = this.statisticsDao.getXLabel(orgCode);
        //计算每个点的值
        for(StatisticsChartDto vo:statisticsXLabelVos){
            String data = this.statisticsDao.fileStatistics(changeDateType(dateType),vo.getId());
            vo.setCnt(data);
        }
        return  changeChartData(statisticsXLabelVos);
    }
    /*public StatisticsFileVo fileStatistics(String dateType) {
        return this.statisticsDao.fileStatistics(dateType);
    }*/

    /**
     * 群用户信息统计
     * @param groupId
     * @return
     */
    @Override
    public  List<StatisticsGroupUserVo> groupUserStatistics(String groupId){
        if(groupId==null){
            groupId="";
        }
        List<StatisticsGroupUserDto> groups = this.statisticsDao.groupUserStatistics(groupId);
        List<StatisticsGroupUserVo> res = new ArrayList<>(16);
        //数据格式转换
        String preGroupId = null;
        boolean isNextGroup = true;//是否下一组统计
        for(int i=0;i<groups.size();i++){
            StatisticsGroupUserDto dto = groups.get(i);
            if(i==0){//如果第一条统计
                preGroupId = dto.getGroupId();
                isNextGroup = true;
            }else{//判断是否下一组
                String newGroupId = dto.getGroupId();
                if(newGroupId.equals(preGroupId)){//如果当前id和前一组id相同
                    isNextGroup = false;
                }else{
                    isNextGroup = true;
                    preGroupId = newGroupId;//更新群id
                }

            }
            if(isNextGroup){//如果是新的群统计
                StatisticsGroupUserVo groupVo = new StatisticsGroupUserVo();
                groupVo.setGroupName(dto.getGroupName());
                StatisticsGroupUserDetailVo userVo = new StatisticsGroupUserDetailVo();
                userVo.setName(dto.getName());
                userVo.setOrgCode(dto.getOrgCode());
                userVo.setPath(dto.getPath());
                List<StatisticsGroupUserDetailVo> userList = new ArrayList<>(16);
                userList.add(userVo);
                groupVo.setUserList(userList);
                groupVo.setGroupUsersCount(userList.size());
                res.add(groupVo);
            }else{//如果继续统计上一个群
                StatisticsGroupUserVo groupVo = res.get(res.size()-1);
                StatisticsGroupUserDetailVo userVo = new StatisticsGroupUserDetailVo();
                userVo.setName(dto.getName());
                userVo.setOrgCode(dto.getOrgCode());
                userVo.setPath(dto.getPath());
                groupVo.getUserList().add(userVo);
                groupVo.setGroupUsersCount(groupVo.getUserList().size());
            }
        }
        return res;
    }
    /**
     * 群org信息统计
     * @param groupId
     * @return
     */
    @Override
    public  List<StatisticsGroupOrgVo> groupOrgStatistics(String groupId){
        if(groupId==null){
            groupId="";
        }
        List<StatisticsGroupOrgDto> groups = this.statisticsDao.groupOrgStatistics(groupId);
        List<StatisticsGroupOrgVo> res = new ArrayList<>(16);
        //数据格式转换
        String preGroupId = null;
        boolean isNextGroup = true;//是否下一组统计
        for(int i=0;i<groups.size();i++){
            StatisticsGroupOrgDto dto = groups.get(i);
            if(i==0){//如果第一条统计
                preGroupId = dto.getGroupId();
                isNextGroup = true;
            }else{//判断是否下一组
                String newGroupId = dto.getGroupId();
                if(newGroupId.equals(preGroupId)){//如果当前id和前一组id相同
                    isNextGroup = false;
                }else{
                    isNextGroup = true;
                    preGroupId = newGroupId;//更新群id
                }

            }
            if(isNextGroup){//如果是新的群统计
                StatisticsGroupOrgVo groupVo = new StatisticsGroupOrgVo();
                groupVo.setGroupName(dto.getGroupName());
                StatisticsGroupOrgDetailVo userVo = new StatisticsGroupOrgDetailVo();
                userVo.setParentId(dto.getPid());
                userVo.setOrgCode(dto.getOrgCode());
                userVo.setPath(dto.getPath());
                List<StatisticsGroupOrgDetailVo> userList = new ArrayList<>(16);
                userList.add(userVo);
                groupVo.setOrgList(userList);
                groupVo.setGroupOrgCount(userList.size());
                res.add(groupVo);
            }else{//如果继续统计上一个群
                StatisticsGroupOrgVo groupVo = res.get(res.size()-1);
                StatisticsGroupOrgDetailVo userVo = new StatisticsGroupOrgDetailVo();
                userVo.setParentId(dto.getPid());
                userVo.setOrgCode(dto.getOrgCode());
                userVo.setPath(dto.getPath());
                groupVo.getOrgList().add(userVo);
                groupVo.setGroupOrgCount(groupVo.getOrgList().size());
            }
        }
        return res;
    }
    /**
     * 数据转换为chat需要的格式
     * @param oriDatas
     * @return
     */
    private List<StatisticsChartDataVo> changeChartData(List<StatisticsChartDto> oriDatas){
        List<StatisticsChartDataVo> res = new ArrayList<>(16);
        for(StatisticsChartDto oriData:oriDatas){
            StatisticsChartDataVo vo = new StatisticsChartDataVo();
            vo.setX(oriData.getOrgName());
            vo.setY(Long.valueOf(oriData.getCnt()));
            res.add(vo);
        }
        return res;
    }

    /**
     * 日期查询转码
     * @param date
     * @return
     */
    private String changeDateType(String date){
        if(date==null || "".equals(date) || "quanbu".equals(date)){
            return "0";
        }else if("jinri".equals(date)){
            return "1";
        }else if("benzhou".equals(date)){
            return "2";
        }else if("benyue".equals(date)){
            return "3";
        }else {
            return "0";
        }
    }
}