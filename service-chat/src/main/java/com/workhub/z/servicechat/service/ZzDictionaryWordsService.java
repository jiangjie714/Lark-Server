package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzDictionaryWords;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 字典词汇表(ZzDictionaryWords)表服务接口
 *
 * @author makejava
 * @since 2019-05-17 14:56:57
 */
public interface ZzDictionaryWordsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZzDictionaryWords queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzDictionaryWords> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzDictionaryWords 实例对象
     * @return 实例对象
     */
    int insert(ZzDictionaryWords zzDictionaryWords);

    /**
     * 修改数据
     *
     * @param zzDictionaryWords 实例对象
     * @return 实例对象
     */
    int update(ZzDictionaryWords zzDictionaryWords);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    void deleteById(String id);

    /**
     * 检索文本是否涉密（涉密类型）
     * @return
     */
    String confidentialIndex(String txt);
    /**
     * 检索文本是否敏感词汇(敏感词汇)
     * @param txt
     * @return
     */
    String sensitiveIndex(String txt);
    //导入敏感词汇
    String importDictionaryWords(MultipartFile file,String userId) throws Exception;
    //停用启用
    int stopUse(String id,String useFlg,String userId) throws  Exception;
    //分页查询
    TableResultResponse<ZzDictionaryWords> query(int page, int size, String type, String code, String name, String replace,String isUse) throws  Exception;
    //查询涉密词汇
    //List<ZzDictionaryWords> getSecretWordList(String type);
    List<ZzDictionaryWords> getSecretWordList40();
    List<ZzDictionaryWords> getSecretWordList60();
}