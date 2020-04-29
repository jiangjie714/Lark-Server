package com.workhub.z.servicechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.entity.config.UserInfo;
import com.workhub.z.servicechat.entity.config.ZzDictionaryWords;
import com.workhub.z.servicechat.model.ContactsMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
*@Description: 通用方法
*@Author: 忠
*@date: 2019/5/14
*/
public class Common {
    private static Logger log = LoggerFactory.getLogger(Common.class);
//  默认图片路径
    public static final String imgUrl = "";
    /**
     * 加密解密算法 执行一次加密，两次解密
     * @param inStr 加密字符
     * @return
     */
    public static String convert(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    /**
     * 校验文本包含非法字符，返回所包含的非法字符以,分割没有返回空串
     * @param str 文本
     * @param index 非法字符
     * @return
     */
    public static String stringSearch(String str,String... index){
        if(null == str) {
            throw new NullPointerException("str is null");
        }
        if(null == index) {
            throw new NullPointerException("index is null");
        }
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(index).filter(indexStr -> str.contains(indexStr)).forEach(indexResult ->{
                stringBuilder.append(indexResult).append(",");
        });
        String resultStr = stringBuilder.toString();
        if (resultStr.length() == 0) {
            return resultStr;
        }
        return resultStr.substring(0,resultStr.length()-1);
    }

    /**
     * 校验文本包含非法字符，返回所包含的非法字符以,分割没有返回空串
     * @param str 文本
     * @return
     */
    public static String stringSearch(String str){
        if(null == str) {
            throw new NullPointerException("str is null");
        }
        Set<String> setIndex = new HashSet<String>();//TODO 来源
        setIndex.add("机密");
        setIndex.add("非密");
        setIndex.add("密级");
        setIndex.add("秘密");
        StringBuilder stringBuilder = new StringBuilder();
        setIndex.stream().filter(setIndexFilter -> str.contains(setIndexFilter)).forEach(setIndexResult ->{
            stringBuilder.append(setIndexResult).append(",");
        });
        String resultStr = stringBuilder.toString();
        if (resultStr.length() == 0) {
            return resultStr;
        }
        return resultStr.substring(0,resultStr.length()-1);
    }

    /**
     * 校验文本包含非法字符，返回所包含的非法字符以,分割没有返回空串
     * @param str 文本
     * @param setStr 非法字符
     * @return
     */
    public static String stringSearch(String str, Set<String> setStr){
        if(null == str) {
            throw new NullPointerException("str is null");
        }
        if(null == setStr) {
            throw new NullPointerException("setStr is null");
        }
        StringBuilder stringBuilder = new StringBuilder();
        setStr.stream().filter(setIndexFilter -> str.contains(setIndexFilter)).forEach(setIndexResult ->{
            stringBuilder.append(setIndexResult).append(",");
        });
        String resultStr = stringBuilder.toString();
        if (resultStr.length() == 0) {
            return resultStr;
        }
        return resultStr.substring(0,resultStr.length()-1);
    }

    /**
     * 涉密检索
     * @param txt
     * @param zzDictionaryWordsList
     * @return
     */
    public static String stringSearch(String txt,List<ZzDictionaryWords> zzDictionaryWordsList) {
        // TODO: 2019/5/31 获取涉密词汇列表
        if(null == txt) {
            throw new NullPointerException("txt is null");
        }
        if(null == zzDictionaryWordsList||zzDictionaryWordsList.isEmpty()) {
            throw new NullPointerException("zzDictionaryWordsList is null");
        }
        Set<String> strSet = new HashSet<String>();
        Optional<ZzDictionaryWords> max = zzDictionaryWordsList.stream()
                    .filter(setIndexFilter -> txt.contains(setIndexFilter.getWordName()))
                    .max(Comparator.comparing(zz ->Integer.parseInt(zz.getWordCode())));
        try {
            return max.get().getWordCode();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    //消息是否可以发送txt消息 zzDictionaryWordsList涉密词汇列表
    public static boolean isMsgApproved(String txt,List<ZzDictionaryWords> zzDictionaryWordsList) {
        if(null == txt) {
            throw new NullPointerException("txt is null");
        }
        if(null == zzDictionaryWordsList) {
            throw new NullPointerException("zzDictionaryWordsList is null");
        }
        for(ZzDictionaryWords zzDictionaryWords:zzDictionaryWordsList){
            if(txt.indexOf(zzDictionaryWords.getWordName())!=-1){//有敏感词汇
                return  false;
            }
        }
        return  true;
    }

    //校验消息密级 type  0只检验到第一个不通过词汇就返回，1全部检验
    public static MessageSecretValidVo checkMessageSecretQuick(String txt, List<ZzDictionaryWords> zzDictionaryWordsList,String matchType) {
        MessageSecretValidVo messageSecretValidVo = new MessageSecretValidVo();
        messageSecretValidVo.setSendStatus("1");
        try {
            putVoNullStringToEmptyString(messageSecretValidVo);
        }catch (Exception e){
            log.error("过滤敏感词汇出错！");
            log.error(getExceptionMessage(e));
        }
        for(ZzDictionaryWords zzDictionaryWords:zzDictionaryWordsList){
            if(txt.indexOf(zzDictionaryWords.getWordName())!=-1){//有敏感词汇
                messageSecretValidVo.setSendStatus("0");
                messageSecretValidVo.setSecretWords(","+zzDictionaryWords.getWordName());
                messageSecretValidVo.setSecretLevels(","+zzDictionaryWords.getWordCode());
                if(matchType.equals("0")){
                    break;
                }
            }
        }
        if(!"".equals(messageSecretValidVo.getSecretWords())){
            messageSecretValidVo.setSecretWords(messageSecretValidVo.getSecretWords().substring(1));
            messageSecretValidVo.setSecretLevels(messageSecretValidVo.getSecretLevels().substring(1));
        }
        return  messageSecretValidVo;
    }
    //校验消息密级-重载
    public static MessageSecretValidVo checkMessageSecretQuick(String txt, List<ZzDictionaryWords> zzDictionaryWordsList) {
        return checkMessageSecretQuick(txt,zzDictionaryWordsList,"0");
    }
    /**
     * 敏感词替换
     * @param txt
     * @param zzDictionaryWordsList
     * @return
     */
    public static String sensitiveSearch(String txt, List<ZzDictionaryWords> zzDictionaryWordsList) {
        if (null == txt) {
            throw new NullPointerException();
        }
        zzDictionaryWordsList.stream().forEach(list ->{
            txt.replace(list.getWordName(),list.getReplaceWord());
        });
        return txt;
    }
    /**
    *@Description: 判断用户是否在线
    *@Param: 上下文ChannelContext
    *@return: boolean
    *@Author: 忠
    *@date: 2019/5/30
    */
    /*public static void checkUserOnline(ChannelContext channelContext,String userId){
        ChannelContext previousChannelContext = Tio.getChannelContextByBsId(channelContext.groupContext, userId);
        if (!channelContext.equals(previousChannelContext) && previousChannelContext != null) {
            previousChannelContext.setAttribute("kickOut", true); // 踢掉的标志
            Tio.unbindBsId(previousChannelContext);
            Tio.remove(previousChannelContext, "服务器断开客户端连接");
            System.out.println("踢掉 {} 已经登录的连接 {}"+ userId + previousChannelContext.getClientNode());
        }

    }*/
    /**
     *@Description: 判断msg数据体是否正确
     *@Param: string 接收的消息
     *@return: boolean
     *@Author: 忠
     *@date: 2019/5/30
     */
    public static boolean isJson(String text){
        return false;
    }
    /**
     *@Description: 把entity null 转换成空字符串,时间转换成当前时间，long转换成0L
     *@Param: string 接收的实体
     *@return: boolean
     *@Author: zhuqz
     *@date: 2019/06/10
     */
    public static<T> void putEntityNullToEmptyString (T enity) throws Exception{
        if(enity==null){
            return;
        }
        //遍历enity类 成员为String类型 属性为空的全部替换为“”
        Field[] fields = enity.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // 获取属性的名字
            String name = fields[i].getName();
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 获取属性的类型
            String type = fields[i].getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.String")) {
                Method m = enity.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    String string = new String("");
                    Object[] objects = new Object[1];
                    objects[0] = string;
                    m.invoke(enity, objects);
                }
            }
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.util.Date")) {
                Method m = enity.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                Date value = (Date) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    Date date = new Date();
                    Object[] objects = new Object[1];
                    objects[0] = date;
                    m.invoke(enity, objects);
                }
            }
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.Double")) {
                Method m = enity.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                Double value = (Double) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    double data = 0.0;
                    Object[] objects = new Object[1];
                    objects[0] = data;
                    m.invoke(enity, objects);
                }
            }
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("long")) {
                boolean hasFun = true;
                Method m = null;
                try{
                    m = enity.getClass().getMethod("get" + name);
                }catch (NoSuchMethodException e){//可能是序列化的long
                    hasFun=false;
                }
                if(!hasFun){
                    continue;
                }
                // 调用getter方法获取属性值
                Long value = (Long) m.invoke(enity);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = enity.getClass().getMethod("set" + name, parameterTypes);
                    long data = 0L;
                    Object[] objects = new Object[1];
                    objects[0] = data;
                    m.invoke(enity, objects);
                }
            }
        }
    }
    //获取当前日期
    public static String getCurrentDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        return  format.format(calendar.getTime());
    }
    //获取上个月第一天
    public static String getBeforeMonthFirstDay(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return  format.format(calendar.getTime());
    }
    //异常信息获取
    public static String getExceptionMessage(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
    //格式化double
    public static String formatDouble2(double d) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return String.valueOf(bg.doubleValue());
    }

    /**
     * 分组聚合
     */
    public static <T> List<List<T>> aggregation(List<T> list,Comparator<? super T> comparator) {
        List<List<T>> lists = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            boolean isContain = false;
            for (int j = 0; j < lists.size(); j++) {
                if (lists.get(j).size() == 0||comparator.compare(lists.get(j).get(0),list.get(i)) == 0) {
                    lists.get(j).add(list.get(i));
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                List<T> newList = new ArrayList<>();
                newList.add(list.get(i));
                lists.add(newList);
            }
        }
        return lists;
    }

    public static void aggregation1(List<ContactsMessageDto> list, Map<String, List<ContactsMessageDto>> map) {//map是用来接收分好的组的
        if (null == list) {
            return;
        }


        String key;
        List<ContactsMessageDto> listTmp;
        for (ContactsMessageDto val : list) {
            key = val.getContactsId();//按这个属性分组，map的Key
            listTmp = map.get(key);
            if (null == listTmp) {
                listTmp = new ArrayList<ContactsMessageDto>();
                map.put(key, listTmp);
            }
            listTmp.add(val);
        }
    }
    /**
     *@Description: 把返回给前端的VO 属性是字符串的且值是null的 转换成空字符串，其它类型在代码自行处理
     *@Param: vo
     *@return: void
     *@Author: zhuqz
     *@date: 2019/06/26
     */
    public static<T> void putVoNullStringToEmptyString (T vo) throws Exception{
        if(vo==null){
            return;
        }
        //遍历enity类 成员为String类型 属性为空的全部替换为“”
        Field[] fields = vo.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // 获取属性的名字
            String name = fields[i].getName();
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 获取属性的类型
            String type = fields[i].getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.String")) {
                Method m = vo.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(vo);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = vo.getClass().getMethod("set" + name, parameterTypes);
                    String string = new String("");
                    Object[] objects = new Object[1];
                    objects[0] = string;
                    m.invoke(vo, objects);
                }
            }
        }
    }
    /**
     *@Description: 把返回给前端的List<VO> 属性是字符串的且值是null的 转换成空字符串，其它类型在代码自行处理
     *@Param: vo
     *@return: void
     *@Author: zhuqz
     *@date: 2019/06/26
     */
    public static<T> void putVoNullStringToEmptyString (List<T> list) throws Exception{
        if(list==null){
            return;
        }
        for(T vo:list){
            //遍历enity类 成员为String类型 属性为空的全部替换为“”
            Field[] fields = vo.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                // 获取属性的名字
                String name = fields[i].getName();
                // 将属性的首字符大写，方便构造get，set方法
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // 获取属性的类型
                String type = fields[i].getGenericType().toString();
                // 如果type是类类型，则前面包含"class "，后面跟类名
                if (type.equals("class java.lang.String")) {
                    Method m = vo.getClass().getMethod("get" + name);
                    // 调用getter方法获取属性值
                    String value = (String) m.invoke(vo);
                    //System.out.println("数据类型为：String");
                    if (value == null) {
                        //set值
                        Class[] parameterTypes = new Class[1];
                        parameterTypes[0] = fields[i].getType();
                        m = vo.getClass().getMethod("set" + name, parameterTypes);
                        String string = new String("");
                        Object[] objects = new Object[1];
                        objects[0] = string;
                        m.invoke(vo, objects);
                    }
                }
            }
        }
    }
    /**
     *@Description: 修改json字符串的某个属性值
     *@Param: json字符串；key,如果是多级用"."拼接，key只支持String；value 修改后新的值.只支持到叶子节点，非叶子节点，没试过，有需要自行测试。
     * 例如：某个json字符串是{"toId":"GMt5H6xf","contactInfo":{"name":"8756","id":"GMt5H6xf"},"username":"myname"}，传入key: contactInfo.id, value: newValue,返回值是：{"toId":"GMt5H6xf","contactInfo":{"name":"8756","id":"newValue"},"username":"myname"}
     *@return: 修改后的json字符串
     *@Author: zhuqz
     *@date: 2019/06/28
     */
    public static String   setJsonStringKeyValue (String jsonStr,String key, Object value) throws Exception{
        if(jsonStr==null){
            return null;
        }
        Map<String,Object> jsonMap= JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
        //System.out.println("before:"+JSONObject.toJSONString(jsonMap));
        jsonMap=setMapKeyValue(jsonMap,key,value);
       // System.out.println("after:"+JSONObject.toJSONString(jsonMap));
        return JSONObject.toJSONString(jsonMap);
    }
    //递归赋值给map的key
    public static Map<String,Object> setMapKeyValue(Map<String,Object> map,String key, Object value) throws  Exception {
        String[] keyArr=key.split("\\.");

        if(keyArr.length!=1){
            String currentKey=keyArr[0];
            Map<String,Object> temp=JSONObject.parseObject(map.get(currentKey).toString());
            map.put(currentKey,setMapKeyValue(temp,key.substring(key.indexOf(".")+1),value));

        }else {
            map.put(keyArr[0],value);
        }
        JSONObject jsonObj=new JSONObject(map);
        return  map;
    }
    //测试setJsonStringKeyValue
/*
    public static void main(String[] args) {
        try {
            setJsonStringKeyValue("{\"toId\":\"GMt5H6xf\",\"atId\":[],\"contactInfo\":{\"name\":\"8756\",\"id\":\"GMt5H6xf\",\"avatar\":\"\",\"memberNum\":4,\"isGroup\":true,\"secretLevel\":30},\"id\":\"69be81ca-f633-45c0-be0c-262d14aa68e1\",\"avatar\":\"http://10.12.97.34:80/undefined\",\"time\":\"2019-06-24T08:36:10.137Z\",\"isGroup\":true,\"fromId\":\"duyukun\",\"content\":{\"extension\":\"jpg\",\"id\":\"398b8662f7694ea28264330e63e79d97\",\"type\":3,\"title\":\"jpg\",\"secretLevel\":30,\"url\":\"/api/chat/zzFileManage/GetFile?fileId=398b8662f7694ea28264330e63e79d97&t=1561193135178\"},\"username\":\"杜宇坤\"}",
            "contactInfo.memberNum",
                    12
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    /**
     *@Description: 获取json字符串的某个属性值
     *@Param: json字符串；key,如果是多级用"."拼接，key只支持String；
     * 例如：某个json字符串是{"toId":"GMt5H6xf","contactInfo":{"name":"8756","id":"GMt5H6xf"},"username":"myname"}，传入key: contactInfo.id 返回值是：GMt5H6xf
     *@return:
     *@Author: zhuqz
     *@date: 2019/07/03
     */
    public static Object getJsonStringKeyValue (String jsonStr,String key) throws Exception{
        if(jsonStr==null){
            return null;
        }
        Map<String,Object> jsonMap= JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
        return getMapKeyValue(jsonMap,key);
    }

    //递归json的key
    public static Object getMapKeyValue(Map<String,Object> map,String key) throws  Exception {
        String[] keyArr=key.split("\\.");

        if(keyArr.length!=1){
            String currentKey=keyArr[0];
            if(map.get(currentKey)==null){
                return null;
            }
            Map<String,Object> temp=JSONObject.parseObject(map.get(currentKey).toString());
            return getMapKeyValue(temp,key.substring(key.indexOf(".")+1));

        }else {
            return map.get(keyArr[0]);
        }
        
    }
/*    public static void main(String[] args) {
        try {
            Object obj = getJsonStringKeyValue("{\"toId\":\"GMt5H6xf\",\"atId\":[],\"contactInfo\":{\"name\":\"8756\",\"id\":\"GMt5H6xf\",\"avatar\":\"\",\"memberNum\":4,\"isGroup\":true,\"secretLevel\":30,\"family\":{\"address\":\"China Shandong\"}},\"id\":\"69be81ca-f633-45c0-be0c-262d14aa68e1\",\"avatar\":\"http://10.12.97.34:80/undefined\",\"time\":\"2019-06-24T08:36:10.137Z\",\"isGroup\":true,\"fromId\":\"duyukun\",\"content\":{\"extension\":\"jpg\",\"id\":\"398b8662f7694ea28264330e63e79d97\",\"type\":3,\"title\":\"jpg\",\"secretLevel\":30,\"url\":\"/api/chat/zzFileManage/GetFile?fileId=398b8662f7694ea28264330e63e79d97&t=1561193135178\"},\"username\":\"杜宇坤\"}",
                    "contactInfo.family.2222"
            );
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    //objec转字符串 null-> ""
    public static String nulToEmptyString(Object object){
        try{
            if(object==null){
                return "";
            }
            return  object.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error(getExceptionMessage(e));
        }
        return  null;
    }
    //objec转字符串 null-> "0"
    public static String nulToZeroString(Object object){
        try{
            if(object==null){
                return "0";
            }
            return  object.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error(getExceptionMessage(e));
        }
        return  null;
    }
    //判断群组织是否跨越场所，参数是所有的成员列表,true跨越场所 false不跨越
    public static boolean  isGroupCross(List<UserInfo> userInfoList){
        boolean iscross = false;
        String userOrgCode="";
        for(UserInfo userInfo:userInfoList){
            if(userOrgCode.equals("")){//遍历的第一条
                if(userInfo.getOrgCode()!=null && !userInfo.getOrgCode().equals("") && userInfo.getOrgCode().length()>=4){
                    userOrgCode = userInfo.getOrgCode().substring(0,4);
                }

            }else{//判断如果组织id不一样，立刻判定是跨场所，终止循环
                if(userInfo.getOrgCode()!=null && !userInfo.getOrgCode().equals("") && userInfo.getOrgCode().length()>=4){
                    if(!userOrgCode.equals(userInfo.getOrgCode().substring(0,4))){
                        iscross = true;
                        break;
                    }
                }

            }
        }
        return iscross;
    }
    //String to List 同时去重复
    public static List<String>  stringToList(String str){
        return stringToList(str,",",true);
    }
    //String to List 同时去重复
    public static List<String>  stringToList(String str,String markFlg){
        return stringToList(str,markFlg,true);
    }
    //String to List 同时去重复
    public static List<String>  stringToList(String str,boolean clearMul){
        return stringToList(str,",",clearMul);
    }
    //String to List 同时去重复 str 待处理字符串；markFlg 分隔符；clearMul是否去重复
    public static List<String>  stringToList(String str,String markFlg , boolean clearMul){
        List<String> res = new ArrayList<>();
        String[] arr = str.split(markFlg);
        if(arr!=null && arr.length>0){

           for(String temp : arr){
               if (clearMul){
                   if(res.contains(temp)){
                       continue;
                   }else{
                       res.add(temp);
                   }
               }else {
                   res.add(temp);
               }

           }
        }
        return res;
    }
    // Clob类型 转String
    public static String ClobToString(Clob clob) throws SQLException, IOException {
        String ret = "";
        Reader read= clob.getCharacterStream();
        BufferedReader br = new BufferedReader(read);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        ret = sb.toString();
        if(br != null){
            br.close();
        }
        if(read != null){
            read.close();
        }
        return ret;
    }
    /**
     * 获取客户端真实IP
     * */
    public static  String getIRealIPAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))    {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)   || "null".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)    || "null".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * getter setter 的object 进行复制 主要针对 dto 转 vo 通用部分
     * @param source 源文件
     * @param dest 目标文件
     */
    public static void copyObject(Object source, Object dest) throws  Exception{
        //获取属性
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class);
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), java.lang.Object.class);
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

        try{
            for(int i=0;i<sourceProperty.length;i++){

                for(int j=0;j<destProperty.length;j++){

                    if(
                            sourceProperty[i].getName().equals(destProperty[j].getName())
                                    && sourceProperty[i].getReadMethod().getReturnType().equals(destProperty[j].getReadMethod().getReturnType())
                    ){
                        //调用source的getter方法和dest的setter方法
                        destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
                        break;
                    }
                }
            }
        }catch(Exception e){
            log.error("复制对象出错！");
            log.error(getExceptionMessage(e));
        }
    }
    /**
     * getter setter 的object 进行复制列表 主要针对 dto 转 vo 通用部分
     * @param sources 源文件
     * @param targetClass 目标文件类型
     */
    public static<T,T2> List<T>  copyObject(List<T2> sources, Class<T> targetClass) throws  Exception{
        List<T> resList = new ArrayList<>(16);
        for(T2 source:sources){
            //获取属性
            BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class);
            PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

            BeanInfo destBean = Introspector.getBeanInfo(targetClass, java.lang.Object.class);
            PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
            T dest = targetClass.newInstance();
            try{
                for(int i=0;i<sourceProperty.length;i++){

                    for(int j=0;j<destProperty.length;j++){

                        if(
                                sourceProperty[i].getName().equals(destProperty[j].getName())
                                        && sourceProperty[i].getReadMethod().getReturnType().equals(destProperty[j].getReadMethod().getReturnType())
                        ){
                            //调用source的getter方法和dest的setter方法
                            destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
                            break;
                        }
                    }
                }
            }catch(Exception e){
                log.error("复制对象出错！");
                log.error(getExceptionMessage(e));
            }
        }
        return resList;
    }

    /**
     * 群或者会议，返回人员增加或者删除列表
     * @param oriList 原来人员列表
     * @param nowList 最新人员列表
     */
    public  static TeamMemberChangeListVo  teamMemberChangeInf(List<String> oriList,List<String> nowList){
        TeamMemberChangeListVo vo = new TeamMemberChangeListVo();
        List<String> addList = new ArrayList<>();
        List<String> delList = new ArrayList<>();
        //新增判断
        for(String now:nowList){
            boolean addFlg = true;//该人员是新增的
            for(String temp: oriList){
                if(temp.equals(now)){
                    addFlg = false;
                    break;
                }
            }
            if(addFlg){
                addList.add(now);
            }

        }
        //删除判断
        for(String temp: oriList){
            boolean removeFlg = true;//该人员是删除的
            for(String now : nowList){
                if(temp.equals(now)){
                    removeFlg = false;
                    break;
                }
            }
            if(removeFlg){
                delList.add(temp);
            }
        }
        vo.setAddList(addList);
        vo.setDelList(delList);
        return vo;
    }

    /**
     * 校验消息合法
     * @param msg
     * @return
     */
    public static CheckSocketMsgVo checkSocketMsg(Object msg){
        //先校验消息体本身基本属性
        CheckSocketMsgVo vo = new CheckSocketMsgVo();
        Class msgClass = msg.getClass();
        Class msgVoClass = SocketMsgVo.class;
        if(msgClass!=msgVoClass){
            vo.setRes(false);
            vo.setMsg("消息不合法");
            log.error(msg.toString());
            log.error("消息不合法");
            return  vo;
        }
        SocketMsgVo msgVo = (SocketMsgVo)msg;
        if(msgVo.getCode()==null){
            vo.setRes(false);
            vo.setMsg("消息没有编码");
            log.error("消息没有编码");
            log.error(msg.toString());
            return  vo;
        }
        Object msgObj = msgVo.getMsg();
        if(msgObj==null){
            vo.setRes(false);
            vo.setMsg("消息没有消息体");
            log.error("消息没有消息体");
            log.error(msg.toString());
            return  vo;
        }
        //校验消息内容
        Class detailClass = msgObj.getClass();
        Class detailVoClass = SocketMsgDetailVo.class;
        if(detailVoClass!=detailClass){
            vo.setRes(false);
            vo.setMsg("消息体不合法");
            log.error(msg.toString());
            log.error("消息体不合法");
            return  vo;
        }
        SocketMsgDetailVo msgDetailVo = (SocketMsgDetailVo)msgObj;
        if(msgDetailVo.getCode()==null){
            vo.setRes(false);
            vo.setMsg("消息体没有编码");
            log.error("消息体没有编码");
            log.error(msg.toString());
            return  vo;
        }
        if(msgDetailVo.getData()==null){
            vo.setRes(false);
            vo.setMsg("消息体没有消息");
            log.error("消息体没有消息");
            log.error(msg.toString());
            return  vo;
        }
        return vo;
    }
    /**
     * 校验消息枚举重复
     * @return
     */
    public static  boolean checkMsgEnumDuplicate()throws Exception{
        List<String> codes = new ArrayList<>();
        for(SocketMsgDetailTypeEnum tempEnum: SocketMsgDetailTypeEnum.values()){
            if(codes.contains(tempEnum.getCode())){
                throw new Exception("消息枚举重复，code："+tempEnum.getCode());
            }
            codes.add(tempEnum.getCode());
        }
        return true;
    }
    /**
     * 获取int随机收
     * @return
     */
    public static  int getIntRandom(int min,int max){
        Random rand = new Random();
        int res = rand.nextInt(max+1)+min;
        return res;
    }
}