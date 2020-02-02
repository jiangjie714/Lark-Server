package com.github.hollykunge.security.common.util;

import com.github.hollykunge.security.common.vo.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树节点方法类
 *
 * @author 协同设计小组
 * @date 2017/6/12
 */
public class TreeUtil {
    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeNode> List<T> bulid(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            setChildren(treeNode,treeNodes);
        }
        return trees;
    }

    public static <T extends TreeNode> List<T> bulidAsParent(List<T> treeNodes, Object root) {
        List<T> trees = treeNodes.stream().filter(t -> Objects.equals(t.getId(), root)).collect(Collectors.toList());
        for (T treeNode : treeNodes) {
            //如果root为treenode的parentid
            if (root.equals(treeNode.getParentId())) {
                trees.get(0).add(treeNode);
            }
            setChildren(treeNode, treeNodes);
        }
        return trees;
    }

    private static <T extends TreeNode> void setChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (it.getParentId().equals(treeNode.getId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new HashSet<>());
                }
                treeNode.add(it);
            }
        }
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new HashSet<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

}
