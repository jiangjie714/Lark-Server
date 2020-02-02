package com.github.hollykunge.security.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * list工具类
 * @author fansq
 * @since  2019-12-16
 */
public class ListUtil<E> {
    public Map<Integer, List<E>> batchList(List<E> list, int batchSize){
        Map<Integer,List<E>> itemMap = new HashMap<>();
        itemMap.put(1, new ArrayList<E>());
        for(E e : list){
            List<E> batchList= itemMap.get(itemMap.size());
            if(batchList.size() == batchSize){//当list满足批次数量，新建一个list存放后面的数据
                batchList = new ArrayList<E>();
                itemMap.put(itemMap.size()+1, batchList);
            }
            batchList.add(e);
        }
        return itemMap;
    }
}
