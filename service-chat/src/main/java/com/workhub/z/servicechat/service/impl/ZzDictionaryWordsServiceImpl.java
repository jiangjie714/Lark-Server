package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzDictionaryWordsDao;
import com.workhub.z.servicechat.entity.config.ZzDictionaryWords;
import com.workhub.z.servicechat.service.ZzDictionaryWordsService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

import static com.workhub.z.servicechat.config.Common.putEntityNullToEmptyString;

/**
 * 字典词汇表(ZzDictionaryWords)表服务实现类
 *
 * @author makejava
 * @since 2019-05-17 14:56:57
 */
@Service("zzDictionaryWordsService")
public class ZzDictionaryWordsServiceImpl implements ZzDictionaryWordsService {
    private static Logger log = LoggerFactory.getLogger(ZzDictionaryWordsServiceImpl.class);
    @Resource
    private ZzDictionaryWordsDao zzDictionaryWordsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZzDictionaryWords queryById(String id) {
        //return this.zzDictionaryWordsDao.queryById(id);
       /* ZzDictionaryWords entity = new ZzDictionaryWords();
        entity.setId(id);
        return  super.selectOne(entity);*/
       ZzDictionaryWords zzDictionaryWords = this.zzDictionaryWordsDao.queryById(id);
        try {
            Common.putVoNullStringToEmptyString(zzDictionaryWords);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        return zzDictionaryWords;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzDictionaryWords> queryAllByLimit(int offset, int limit) {
        return this.zzDictionaryWordsDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zzDictionaryWords 实例对象
     * @return 实例对象 0表示数据已经存在，无法重复添加
     */
    @Caching(evict={@CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_40 +"'"),
                    @CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_60 +"'")})
    @Override
    //@CacheClear(keys = {SECRET_WORDS_40,SECRET_WORDS_60})
    public int insert(ZzDictionaryWords zzDictionaryWords) {
        try {
            putEntityNullToEmptyString(zzDictionaryWords);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long lo  = this.zzDictionaryWordsDao.selcount("",zzDictionaryWords.getWordName(),zzDictionaryWords.getWordType());
        if(lo >0){//如果已经存在记录
            return 0;
        }
         int insert=this.zzDictionaryWordsDao.insert(zzDictionaryWords);
        return insert;
        //super.insert(zzDictionaryWords);
    }

    /*@Override
    protected String getPageName() {
        return null;
    }
*/
    /**
     * 修改数据
     *
     * @param zzDictionaryWords 实例对象
     * @return 实例对象
     */
    @Caching(evict={@CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_40 +"'"),
                    @CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_60 +"'")})
    /*@CacheClear(keys = {SECRET_WORDS_40,SECRET_WORDS_60})*/
    @Override
    public int update(ZzDictionaryWords zzDictionaryWords) {
        long insert = this.zzDictionaryWordsDao.selcount(zzDictionaryWords.getId(),zzDictionaryWords.getWordName(),zzDictionaryWords.getWordType());
        if(insert>0){//如果已经存在记录
            return 0;
        }
        if(zzDictionaryWords.getWordType().equals("1")){//涉密词汇
            zzDictionaryWords.setReplaceWord("");//替换词清空
        }
        if(zzDictionaryWords.getWordType().equals("2")){//敏感词汇
            zzDictionaryWords.setWordCode("");
        }
        int update = this.zzDictionaryWordsDao.update(zzDictionaryWords);
        return  update;
        //return update;
        //super.updateById(zzDictionaryWords);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Caching(evict={@CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_40 +"'"),
            @CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_60 +"'")})
    /*@CacheClear(keys = {SECRET_WORDS_40,SECRET_WORDS_60})*/
    @Override
    public void deleteById(String id) {
        //return this.zzDictionaryWordsDao.deleteById(id) > 0;
        /*ZzDictionaryWords entity = new ZzDictionaryWords();
        entity.setId(id);
        super.delete(entity);*/
        this.zzDictionaryWordsDao.deleteById(id);
    }


    @Override
    public String sensitiveIndex(String txt) {
        ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
        zzDictionaryWords.setWordType("SENSITIVE");
        zzDictionaryWords.setIsUse("1");
        List<ZzDictionaryWords> zzDictionaryWordsList = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
        if (null == zzDictionaryWordsList && zzDictionaryWordsList.isEmpty()) {
            return txt;
        }
        return Common.sensitiveSearch(txt,zzDictionaryWordsList);
    }

    @Override
    public String confidentialIndex(String txt) {
        ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
        zzDictionaryWords.setWordType("CONFIDENTIAL");
        zzDictionaryWords.setIsUse("1");
        List<ZzDictionaryWords> zzDictionaryWordsList = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
        if (null == zzDictionaryWordsList && zzDictionaryWordsList.isEmpty()) {
            return "";
        }

//        Set<String> strSet = new HashSet<String>();
//        zzDictionaryWordsList.stream().forEach(zzDictionaryWordsListfor ->{
//            strSet.add(zzDictionaryWordsListfor.getWordName());
//        });
        return Common.stringSearch(txt, zzDictionaryWordsList);
    }

    //导入敏感词汇
    //如果模板内有重复数据，只会导入第一条校验通过的数据
    //如果数据库已经存在词汇，那么不会做任何操作
    @Caching(evict={@CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_40 +"'"),
            @CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_60 +"'")})
    @Override
    /*@CacheClear(keys = {SECRET_WORDS_40,SECRET_WORDS_60})*/
    public String importDictionaryWords(MultipartFile file, String userId) throws Exception{
        Workbook wb = ExcelUtil.getWb(file);
        Map<Integer,Object> headerMap = new HashMap<>();
        Map<Integer, Map<Integer,Object>> datamap = new HashMap<>();
        StringBuilder info = new StringBuilder();
        List<ZzDictionaryWords> dbList=new ArrayList<>();
        List<String> wordsRepeat = new ArrayList<>();//重复校验

        //第1、2行是标题头
        int titleRowNum=2;
        try {
            headerMap=ExcelUtil.readExcelSingleRow(wb,1);
            if(!(headerMap.get(0).toString()).equals("词汇类型")){
                info.append("敏感词汇导入：模板错误"+"/r/n");
               log.error("敏感词汇导入：模板错误");
               throw new Exception("模板错误");
            }
            if(!(headerMap.get(1).toString()).equals("词汇编码")){
                info.append("敏感词汇导入：模板错误"+"/r/n");
                log.error("敏感词汇导入：模板错误");
                throw new Exception("模板错误");
            }
            if(!(headerMap.get(2).toString()).equals("词汇名称")){
                info.append("敏感词汇导入：模板错误"+"/r/n");
                log.error("敏感词汇导入：模板错误");
                throw new Exception("模板错误");
            }
            if(!(headerMap.get(3).toString()).equals("替换词汇")){
                info.append("敏感词汇导入：模板错误"+"/r/n");
                log.error("敏感词汇导入：模板错误");
                throw new Exception("模板错误");
            }

            datamap = ExcelUtil.readExcelContentz(wb,titleRowNum);
        } catch (Exception e) {
            log.error(Common.getExceptionMessage(e));
            e.printStackTrace();
            info.append(Common.getExceptionMessage(e));
        }
        //excel数据存在map里，map.get(0).get(0)为excel(实际存放数据区域，比如标题头不算工作区)第1行第1列的值，此处可对数据进行处理
        if(datamap!=null && datamap.size()!=0){
            //System.out.println(datamap.get(0).get(0));
            Set<Integer> rowSetKey=datamap.keySet();
            Iterator<Integer> itRow = rowSetKey.iterator();
            while (itRow.hasNext()) {
                boolean validFlg=true;//本行校验
                int rowN = itRow.next();//行号
                Map<Integer,Object> rowD=datamap.get(rowN);//行数据
                String wordType = (rowD.get(2)==null)?"":rowD.get(0).toString();//词汇类型
                String wordCode = (rowD.get(2)==null)?"":rowD.get(1).toString();//词汇编码
                String wordName = (rowD.get(2)==null)?"":rowD.get(2).toString();//词汇名称
                String wordRep = (rowD.get(2)==null)?"":rowD.get(3).toString();//替换词汇
                if(wordName.equals("")){
                    info.append("第"+(rowN+titleRowNum)+"行词汇名称没有填写"+"/r/n");
                    validFlg=false;
                }
                if(wordType.equals("")){
                    info.append("第"+(rowN+titleRowNum)+"行词汇类型没有填写"+"/r/n");
                    validFlg=false;
                }
                if(wordType.equals("涉密") && wordCode.equals("")){
                    info.append("第"+(rowN+titleRowNum)+"行词汇类型是涉密，需要填写词汇编码"+"/r/n");
                    validFlg=false;
                }
                if(wordType.equals("敏感") && wordRep.equals("")){
                    info.append("第"+(rowN+titleRowNum)+"行词汇类型是敏感，需要填写替换词汇"+"/r/n");
                    validFlg=false;
                }
                //重复校验
                if(!wordType.equals("") && !wordName.equals("")){
                     if(wordsRepeat.contains(wordType+"000000"+wordName)){
                         info.append("第"+(rowN+titleRowNum)+"行词汇["+wordName+"]重复"+"/r/n");
                         validFlg=false;
                     }
                }
                if(!validFlg){//如果校验不通过，跳转下一行
                    continue;
                }
                wordsRepeat.add(wordType+"000000"+wordName);
                ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
                zzDictionaryWords.setId(RandomId.getUUID());
                zzDictionaryWords.setWordType(DataDictionary.wordTypeDicCons.get(wordType).toString());
                zzDictionaryWords.setCreateTime(new Date());
                zzDictionaryWords.setCreateUser(userId);
                zzDictionaryWords.setReplaceWord(wordRep);
                zzDictionaryWords.setWordCode(DataDictionary.wordCodeDicCons.get(wordCode).toString());
                zzDictionaryWords.setWordName(wordName);

                if(wordType.equals("涉密")){//如果是涉密词汇，去掉替换词
                    zzDictionaryWords.setReplaceWord("");
                }
                if(wordType.equals("敏感")){//如果是敏感词汇，去掉词汇编码
                    zzDictionaryWords.setWordCode("");
                }
                dbList.add(zzDictionaryWords);
            }
        }else{
            info.append("excel文件没有数据");
        }
        /*if(dbList.size()!=0){
            for (int i = 0; i < dbList.size() ; i++) {
                int j = this.zzDictionaryWordsDao.importData(dbList.get(i));
            }
        }*/
        int x=this.zzDictionaryWordsDao.importDataList(dbList);
        if(info.length()==0){
            info.append("共导入数据："+dbList.size()+"条。");
        }else{
            info.insert(0,"共导入数据："+dbList.size()+"条。其他信息：");
        }
        return  info.toString();
    }
    @Caching(evict={@CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_40 +"'"),
            @CacheEvict(value = CacheConst.SECRET_WORDSCACHE, key = "'"+ CacheConst.SECRET_WORDS_60 +"'")})
    @Override
    /*@CacheClear(keys = {SECRET_WORDS_40,SECRET_WORDS_60})*/
    //停用启用
    public  int stopUse(String id,String useFlg,String userId) throws  Exception{
             int res = this.zzDictionaryWordsDao.stopUse(id,useFlg,userId);
             return res;
    }
    //分页查询
    @Override
    public TableResultResponse<ZzDictionaryWords> query(int page, int size, String type, String code, String name, String replace, String isUse) throws  Exception{
        PageHelper.startPage(page, size);
        List<ZzDictionaryWords> dataList =this.zzDictionaryWordsDao.query(type,code,name,replace,isUse);
        //null的String类型属性转换空字符串
        Common.putVoNullStringToEmptyString(dataList);
        PageInfo<ZzDictionaryWords> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<ZzDictionaryWords> res = new TableResultResponse<ZzDictionaryWords>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }
   /* //查询涉密词汇秘密-秘密40 机密60 基于代码缓存
    public List<ZzDictionaryWords> getSecretWordList(String type){
        String RedisKey = type.equals("40")?SECRET_WORDS_40:SECRET_WORDS_60;
        RedisSerializer resdisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(resdisSerializer);
        List<ZzDictionaryWords> list = (List<ZzDictionaryWords>) redisTemplate.opsForValue().get(RedisKey);
        if(list == null){
            synchronized (this){
                list = (List<ZzDictionaryWords>) redisTemplate.opsForValue().get(RedisKey);
                if(list == null){
                    ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
                    zzDictionaryWords.setWordType("1");
                    zzDictionaryWords.setIsUse("1");
                    zzDictionaryWords.setWordCode(type);
                    list = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
                    redisTemplate.opsForValue().set(RedisKey, list);
                }
            }
        }
        return list;
    }*/
    //基于注解缓存
    //获取涉密词汇-秘密
    @Override
    @Cacheable(value=CacheConst.SECRET_WORDSCACHE, key = "'"+CacheConst.SECRET_WORDS_40+"'",sync = true)
    public List<ZzDictionaryWords> getSecretWordList40(){
        log.info("从数据库读取涉密词汇-秘密");
        ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
        zzDictionaryWords.setWordType("1");
        zzDictionaryWords.setIsUse("1");
        zzDictionaryWords.setWordCode("40");
        List<ZzDictionaryWords> list = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
        //redisTemplate.opsForValue().set("chat_secret_words_40", list);
        return list;
    }
    /*//基于注解缓存
    //获取涉密词汇-秘密
    @Override
    @Cache(key = SECRET_WORDS_40,expire = 120)
    public List<ZzDictionaryWords> getSecretWordList40(){
        log.info("从数据库读取涉密词汇-秘密");
        ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
        zzDictionaryWords.setWordType("1");
        zzDictionaryWords.setIsUse("1");
        zzDictionaryWords.setWordCode("40");
        List<ZzDictionaryWords> list = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
        return list;
    }*/
    //基于注解缓存
    //获取涉密词汇-机密
    @Override
    @Cacheable(value=CacheConst.SECRET_WORDSCACHE, key = "'"+CacheConst.SECRET_WORDS_60+"'",sync = true)
    public List<ZzDictionaryWords> getSecretWordList60(){
        log.info("从数据库读取涉密词汇-机密");
        ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
        zzDictionaryWords.setWordType("1");
        zzDictionaryWords.setIsUse("1");
        zzDictionaryWords.setWordCode("60");
        List<ZzDictionaryWords> list = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
        //redisTemplate.opsForValue().set("chat_secret_words_60", list);
        return list;
    }
    /*//基于注解缓存
    //获取涉密词汇-机密
    @Override
    @Cache(key = SECRET_WORDS_60,expire = 120)
    public List<ZzDictionaryWords> getSecretWordList60(){
        log.info("从数据库读取涉密词汇-机密");
        ZzDictionaryWords zzDictionaryWords = new ZzDictionaryWords();
        zzDictionaryWords.setWordType("1");
        zzDictionaryWords.setIsUse("1");
        zzDictionaryWords.setWordCode("60");
        List<ZzDictionaryWords> list = this.zzDictionaryWordsDao.queryAll(zzDictionaryWords);
        return list;
    }*/
}