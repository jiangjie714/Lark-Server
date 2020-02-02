package com.github.hollykunge.security.admin.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 集合的util
 * @since: Create in 14:40 2019/12/11
 */
public final class ListUtil {
    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }
        //返回结果
        List<List<T>> result = new ArrayList<List<T>>();
        //传入集合长度
        int size = list.size();
        //分隔后的集合个数
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}
