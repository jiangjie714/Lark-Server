package com.workhub.z.servicechat.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//存放数据词典
public class DataDictionary {
    //数据词典涉密类型
    private final static Map<String,String> wordTypeDic=new HashMap<>();
    public static Map wordTypeDicCons = null;
    static {
        wordTypeDic.put("涉密","1");
        wordTypeDic.put("敏感","2");
        wordTypeDic.put("","");
        wordTypeDic.put(null,"");
        wordTypeDicCons=Collections.unmodifiableMap(wordTypeDic);
    }
    //数据字典词汇编码
    private final static Map<String,String> wordCodeDic=new HashMap<>();
    public static Map wordCodeDicCons = null;
    static {
        wordCodeDic.put("秘密","40");
        wordCodeDic.put("机密","60");
        wordCodeDic.put("","");
        wordCodeDic.put(null,"");
        wordCodeDicCons=Collections.unmodifiableMap(wordCodeDic);
    }
}
