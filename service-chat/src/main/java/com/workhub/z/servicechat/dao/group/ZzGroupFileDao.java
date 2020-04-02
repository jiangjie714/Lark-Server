package com.workhub.z.servicechat.dao.group;

import com.workhub.z.servicechat.VO.FileMonitoringVO;
import com.workhub.z.servicechat.VO.GroupFileVo;
import com.workhub.z.servicechat.entity.group.ZzGroupFile;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 群文件(ZzGroupFile)表数据库访问层
 *
 * @author 忠
 * @since 2019-05-13 10:59:08
 */
public interface ZzGroupFileDao extends Mapper<ZzGroupFile> {

    /**
     * 通过ID查询单条数据
     *
     * @param fileId 主键
     * @return 实例对象
     */
    ZzGroupFile queryById(String fileId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzGroupFile> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zzGroupFile 实例对象
     * @return 对象列表
     */
    List<ZzGroupFile> queryAll(ZzGroupFile zzGroupFile);

    /**
     * 新增数据
     *
     * @param zzGroupFile 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ZzGroupFile zzGroupFile);

    /**
     * 修改数据
     *
     * @param zzGroupFile 实例对象
     * @return 影响行数
     */
    int update(ZzGroupFile zzGroupFile);

    /**
     * 通过主键删除数据
     *
     * @param fileId 主键
     * @return 影响行数
     */
    int deleteById(String fileId);

    //List<GroupInfoVO> groupFileList(@Param("id") String id, @Param("start") Integer start, @Param("end") Integer end);

    Long groupFileListTotal(@Param("id")String id);


    List<GroupFileVo> groupFileList(@Param("id") String id,@Param("userId") String userId,@Param("query") String query);

    List<GroupFileVo> groupFileList(@Param("id") String id,@Param("query") String query);

    List<GroupFileVo> groupFileListByMe(@Param("groupId") String groupId,@Param("userId") String userId);

    List<GroupFileVo> groupFileListByOwner(@Param("groupId") String groupId,@Param("userId") String userId);

    List<GroupFileVo> groupFileListByPass(@Param("groupId") String groupId);

    //查询附件大小
    double queryFileSize(@Param("dateFmat") String dateFmat,@Param("date") String date,@Param("unit") long unit );
    //查询附件大小(日期范围)
    List<Map> queryFileSizeRange(@Param("dateFmat") String dateFmat, @Param("dateBegin") String dateBegin, @Param("dateEnd") String dateEnd, @Param("unit") long unit );
    //上传文件记录
    int fileRecord(@Param("param") ZzGroupFile zzUploadFile);
    //文件信息补全
    int fileUpdate(@Param("param") ZzGroupFile zzUploadFile);
    List<FileMonitoringVO> fileMonitoring(@Param("params")  Map<String,Object> param);

    int setFileApproveFLg(@Param("params") List<Map<String,String>> params);
    /**
     * 查询文件信息列表
     * @param params page、size页码页数 ；groupId接收人id ；userId 上传人id ；isGroup文件类型0私聊1群文件905会议文件
     * @return
     * @throws Exception
     */
    List<GroupFileVo> getGroupFileList(@Param("params") Map<String,String> params);
}