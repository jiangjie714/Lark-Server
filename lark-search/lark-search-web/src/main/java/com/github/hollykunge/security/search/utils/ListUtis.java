package com.github.hollykunge.security.search.utils;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 11:08 2020/3/17
 */
public class ListUtis {
    public static List transferList(Iterable iterable){
        List result = new ArrayList();
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()){
            result.add(iterator.next());
        }
        return result;
    }
}
