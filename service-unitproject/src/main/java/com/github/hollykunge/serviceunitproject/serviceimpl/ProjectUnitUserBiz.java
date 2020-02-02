package com.github.hollykunge.serviceunitproject.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.util.TreeUtil;
import com.github.hollykunge.serviceunitproject.common.AdminUser;
import com.github.hollykunge.serviceunitproject.common.LevelUser;
import com.github.hollykunge.serviceunitproject.common.ProjectUnitTree;
import com.github.hollykunge.serviceunitproject.common.ProjectUnitUser;
import com.github.hollykunge.serviceunitproject.dao.ProjectUnitMapper;
import com.github.hollykunge.serviceunitproject.dao.UserMapper;
import com.github.hollykunge.serviceunitproject.model.ProjectUnit;
import com.github.hollykunge.serviceunitproject.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author guxq
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectUnitUserBiz extends BaseBiz<ProjectUnitMapper, ProjectUnit> {

    @Resource
    private ProjectUnitMapper projectUnitMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    protected String getPageName() {
        return null;
    }

    public List getProjectUnit(String unitID, String secretLevels, String pID) {
        List<ProjectUnit> projectUnits = this.selectListAll();
        //   Collections.sort(projectUnits, Comparator.comparing(ProjectUnit::getCseq));
        //List<ProjectUnitTree> projectUnitTrees = this.getTree(projectUnits, unitID, pID);
        //   List<ProjectUnit> projectUnitsPritys = JSON.parseArray(JSON.toJSONString(projectUnits), ProjectUnit.class);
        List<LevelUser> userlistid = this.buildByRecursive(projectUnits, unitID);
        List<LevelUser> userDistinctList = RemoveRepeatData(userlistid, pID);
        List<AdminUser> userlist = getConverIntoAdminUser(userDistinctList, secretLevels);
        return userlist;
    }

    private List<AdminUser> getConverIntoAdminUser(List<LevelUser> userlistpid, String secretLevels) {

       // User userParams = new User();
        Example userExample = new Example(User.class);
        Collections.sort(userlistpid, Comparator.comparing(LevelUser::getLevelType));
        Example.Criteria criteria = userExample.createCriteria();
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < userlistpid.size(); i++) {
            LevelUser lu = userlistpid.get(i);
            String pid = lu.getPId();
            criteria.getAllCriteria().clear();
            criteria.andEqualTo("pId", pid);
            if (!StringUtils.isEmpty(secretLevels)) {
                String[] secretLevelArray = secretLevels.split(",");
                List<String> secretList = Arrays.asList(secretLevelArray);
                for (String secretLevel : secretLevelArray) {
                    criteria.andIn("secretLevel", secretList);
                }
            }
            List<User> usertemp = userMapper.selectByExample(userExample);
            for (User u : usertemp) {
                u.setTecPost(lu.getLevelType());
                u.setWorkPost(lu.getLevelTypeDescript());
                users.add(u);
            }
        }

        List<AdminUser> userList;
        userList = JSON.parseArray(JSON.toJSONString(users), AdminUser.class);
        return userList;
    }

    private List buildByRecursive(List<ProjectUnit> projectUnits, Object root) {
        //  List<ProjectUnitUser> result = new ArrayList<>();
        List<ProjectUnitUser> treeNodes;
        List<LevelUser> userlistid = new ArrayList();

        treeNodes = JSON.parseArray(JSON.toJSONString(projectUnits), ProjectUnitUser.class);
        for (int i = 0; i < treeNodes.size(); i++) {
            ProjectUnitUser treeNode = treeNodes.get(i);

            //当前节点
            if (root.equals(treeNode.getId())) {

                getUserListid(treeNode, userlistid);
                //找总师
                getZongShiIDByParent(treeNode, treeNodes, userlistid);
            }
            //子节点
     /*       if (root.equals(treeNode.getParentId())) {
                ProjectUnitUser children = findChildren(treeNode, treeNodes, userlistid);
                result.add(children);
            }*/

        }
        return userlistid;
    }

    private void getZongShiIDByParent(ProjectUnitUser treeNode, List<ProjectUnitUser> treeNodes, List<LevelUser>
            userlistid) {
        for (ProjectUnitUser it : treeNodes) {
            if (treeNode.getParentId().equals(it.getId())) {
                //    String zongshiname=treeNode.getName();
                String zongshiid = treeNode.getDdOrgUserId();
                if (!StringUtils.isEmpty(zongshiid)) {
                    String[] zongshiidarray = zongshiid.split(",");
                    for (int i = 0; i < zongshiidarray.length; i++) {
                        LevelUser lu = new LevelUser();
                        lu.setLevelType("1");
                        lu.setPId(zongshiidarray[i]);
                        lu.setLevelTypeDescript("副总师");
                        userlistid.add(lu);
                    }
                }
                getZongShiIDByParent(it, treeNodes, userlistid);

            }


        }
    }

    private void getUserListid(ProjectUnitUser treeNode, List<LevelUser> userlistid) {

       // String zongshiid = treeNode.getDdOrgUserId();
        String orgUserId = treeNode.getOrgUserId();

     /*   if (!StringUtils.isEmpty(zongshiid)) {

            String[] zongshiidarray = zongshiid.split(",");
            for (int i = 0; i < zongshiidarray.length; i++) {
                LevelUser lu = new LevelUser();
                lu.setLevelType("1");
                lu.setPId(zongshiidarray[i]);
                lu.setLevelTypeDescript("副总师");
                userlistid.add(lu);
            }
        }*/
        if (!StringUtils.isEmpty(orgUserId)) {
            LevelUser lu = new LevelUser();
            lu.setLevelType("3");
            lu.setLevelTypeDescript("技术负责人");
            lu.setPId(orgUserId);
            userlistid.add(lu);
        }
        String partaskuserid = treeNode.getPartaskOrgUserId();
        if (!StringUtils.isEmpty(partaskuserid)) {
            String[] partaskuseronearray = partaskuserid.split(",");
            for (int i = 0; i < partaskuseronearray.length; i++) {
                LevelUser lu = new LevelUser();
                lu.setLevelType("4");
                lu.setPId(partaskuseronearray[i]);
                lu.setLevelTypeDescript("团队成员");
                userlistid.add(lu);
            }
        }

        String ddpartuserid = treeNode.getDdPartaskUserId();
        if (!StringUtils.isEmpty(ddpartuserid)) {
            String[] ddpartaskuseronearray = ddpartuserid.split(",");
            for (int i = 0; i < ddpartaskuseronearray.length; i++) {
                LevelUser lu = new LevelUser();
                lu.setLevelType("4");
                lu.setPId(ddpartaskuseronearray[i]);
                lu.setLevelTypeDescript("团队成员");
                userlistid.add(lu);
            }
        }

        String superiorid = treeNode.getSuperior();
        if (!StringUtils.isEmpty(superiorid)) {
            String[] superioridarray = superiorid.split(",");
            for (int i = 0; i < superioridarray.length; i++) {
                LevelUser lu = new LevelUser();
                lu.setLevelType("2");
                lu.setPId(superioridarray[i]);
                lu.setLevelTypeDescript("上级负责人");
                userlistid.add(lu);
            }
        }

        String subordinateid = treeNode.getSubordinate();
        if (!StringUtils.isEmpty(subordinateid)) {
            String[] subordinateidarray = subordinateid.split(",");
            for (int i = 0; i < subordinateidarray.length; i++) {
                LevelUser lu = new LevelUser();
                lu.setLevelType("5");
                lu.setPId(subordinateidarray[i]);
                lu.setLevelTypeDescript("下级负责人");
                userlistid.add(lu);
            }
        }

        String relatedUserId = treeNode.getRelatedUserId();
        if (!StringUtils.isEmpty(relatedUserId)) {
            String[] relatedUserIdarray = relatedUserId.split(",");
            for (int i = 0; i < relatedUserIdarray.length; i++) {
                LevelUser lu = new LevelUser();
                lu.setLevelType("6");
                lu.setPId(relatedUserIdarray[i]);
                lu.setLevelTypeDescript("相关方");
                userlistid.add(lu);
            }
        }
    }

    private List<LevelUser> RemoveRepeatData(List<LevelUser> userlistid, String pid) {
        Collections.sort(userlistid, Comparator.comparing(LevelUser::getLevelType));

        String levelTypeDescripts = "";
        for (int i = 0; i < userlistid.size(); i++) {
            levelTypeDescripts = (String) userlistid.get(i).getLevelTypeDescript();
            for (int j = i + 1; j < userlistid.size(); j++) {
                if ((userlistid.get(i).getPId()).equals(userlistid.get(j).getPId())) {
                    levelTypeDescripts = levelTypeDescripts + "+" + userlistid.get(j).getLevelTypeDescript();
                    userlistid.remove(j);
                    j--;
                }

            }
            userlistid.get(i).setLevelTypeDescript(levelTypeDescripts);
            //前台需要去掉当前用户
            if (userlistid.get(i).getPId().equals(pid)) {
                userlistid.remove(i);
                i--;
            }

        }
        return userlistid;
    }

    private ProjectUnitUser findChildren(ProjectUnitUser treeNode, List<ProjectUnitUser> treeNodes, List<LevelUser>
            userlistid) {

        getUserListid(treeNode, userlistid);
        for (ProjectUnitUser it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {

                getUserListid(it, userlistid);
                ProjectUnitUser children = findChildren(it, treeNodes, userlistid);

            }


        }

        return treeNode;
    }

    public List<ProjectUnitTree> getLastTree(List<ProjectUnit> projectUnits, String parentTreeId, String pid) {
        List<ProjectUnitTree> lastTrees = getTree(projectUnits, parentTreeId, pid);
        return TreeUtil.bulid(lastTrees, parentTreeId);

    }

    public List<ProjectUnitTree> getTree(List<ProjectUnit> projectUnits, String parentTreeId, String pid) {
        List<ProjectUnitTree> trees = new ArrayList<ProjectUnitTree>();
        List<ProjectUnitTree> midtrees = new ArrayList<ProjectUnitTree>();

        ProjectUnitTree node;
        for (ProjectUnit projectUnit : projectUnits) {
            node = new ProjectUnitTree();
            String jsonNode = JSON.toJSONString(projectUnit);
            node = JSON.parseObject(jsonNode, ProjectUnitTree.class);
            if (!StringUtils.isEmpty(projectUnit.getEnabled())) {
                if (projectUnit.getEnabled().trim().equals("1")) {
                    boolean blen = getJudge(projectUnit, pid);
                    if (blen) {
                        node.setLabel(projectUnit.getName());
                        if (!StringUtils.isEmpty(projectUnit.getCseq()))
                            node.setOrder(Long.parseLong(projectUnit.getCseq()));
                        else
                            node.setOrder(Long.parseLong("999999999"));
                        node.setType(projectUnit.getType());
                        midtrees.add(node);
                        trees.add(node);


                    }
                }
            }
        }
        //遍历子节点
        findLoopChildren(projectUnits, midtrees, trees);
        List<ProjectUnitTree> lastTrees = RemoveRepeatTreeData(trees);
        Collections.sort(lastTrees, Comparator.comparing(ProjectUnitTree::getOrder));
        //   List<ProjectUnitTree> unitTrees = TreeUtil.bulid(trees, parentTreeId);
        //  List<ProjectUnitTree>  unitNotProject =removeTypeNotSys(unitTrees);
        return lastTrees;
    }

    private List<ProjectUnitTree> RemoveRepeatTreeData(List<ProjectUnitTree> trees) {

        for (int i = 0; i < trees.size(); i++) {

            for (int j = i + 1; j < trees.size(); j++) {
                if (trees.get(i).getId().equals(trees.get(j).getId())) {
                    trees.remove(j);
                    j--;
                }
            }

        }
        return trees;
    }

    private List<ProjectUnitTree> findLoopChildren(List<ProjectUnit> projectUnits, List<ProjectUnitTree> midtrees, List<ProjectUnitTree> trees) {
        ProjectUnitTree node;
        List<ProjectUnitTree> treeNodes;
        treeNodes = JSON.parseArray(JSON.toJSONString(projectUnits), ProjectUnitTree.class);
        for (int i = 0; i < midtrees.size(); i++) {
            node = new ProjectUnitTree();
            node = midtrees.get(i);
            findTreeChildren(node, treeNodes, trees);
            String type =node.getType();
            if (!StringUtils.isEmpty(type)) {
                if (type.equals("2")) {
                    node.setParentId("root");
                   continue;
                }
            }
            findTreeParent(node, treeNodes, trees);

        }
        return trees;
    }


    private void findTreeChildren(ProjectUnitTree projectUnitTree, List<ProjectUnitTree> treeNodes, List<ProjectUnitTree> trees) {
        for (ProjectUnitTree ptree : treeNodes) {
            if (projectUnitTree.getId().equals(ptree.getParentId())) {
                if (!StringUtils.isEmpty(ptree.getEnabled())) {
                    if (ptree.getEnabled().trim().equals("1")) {
                        ptree.setLabel(ptree.getName());
                        if (!StringUtils.isEmpty(ptree.getCseq()))
                            ptree.setOrder(Long.parseLong(ptree.getCseq()));
                        else
                            ptree.setOrder(Long.parseLong("999999999"));
                        trees.add(ptree);
                    }
                    findTreeChildren(ptree, treeNodes, trees);
                }
            }
        }
    }

    private void findTreeParent(ProjectUnitTree projectUnitTree, List<ProjectUnitTree> treeNodes, List<ProjectUnitTree> trees) {
        for (ProjectUnitTree it : treeNodes) {
            if (projectUnitTree.getParentId().equals(it.getId())) {

                it.setLabel(it.getName());
                if (!StringUtils.isEmpty(it.getCseq()))
                    it.setOrder(Long.parseLong(it.getCseq()));
                else
                    it.setOrder(Long.parseLong("999999999"));
                trees.add(it);
                String type = it.getType();
                if (!StringUtils.isEmpty(type)) {
                    if (type.equals("2")) {
                        it.setParentId("root");
                        return;
                    }
                }

                findTreeParent(it, treeNodes, trees);
            }
        }
    }


    private boolean getJudge(ProjectUnit projectUnit, String pid) {
        boolean bl = false;
        String zongshiid = projectUnit.getDdOrgId();
        if (!StringUtils.isEmpty(zongshiid)) {

            String[] zongshiidarray = zongshiid.split(",");
            for (int i = 0; i < zongshiidarray.length; i++) {
                if (pid.equals(zongshiidarray[i])) {
                    bl = true;
                }
            }
        }
        String orgUserId = projectUnit.getOrgUserId();
        if (pid.equals(orgUserId)) {
            bl = true;
        }
        String partaskuserid = projectUnit.getPartaskOrgUserId();
        if (!StringUtils.isEmpty(partaskuserid)) {
            String[] ddpartaskuseronearray = partaskuserid.split(",");
            for (int i = 0; i < ddpartaskuseronearray.length; i++) {
                if (pid.equals(ddpartaskuseronearray[i])) {
                    bl = true;
                }

            }
        }
        String ddpartuserid = projectUnit.getDdPartaskUserId();
        if (!StringUtils.isEmpty(ddpartuserid)) {
            String[] ddpartaskuseronearray = ddpartuserid.split(",");
            for (int i = 0; i < ddpartaskuseronearray.length; i++) {
                if (pid.equals(ddpartaskuseronearray[i])) {
                    bl = true;
                }
            }
        }
        return bl;
    }

  /*  private List<ProjectUnitTree> removeTypeNotSys(List<ProjectUnitTree> utree ){
        for(int i=0;i<utree.size();i++){
            if(!"1".equals(utree.get(i))){
                utree.remove(i);
            }
        }
        return utree;
    }
*/
}