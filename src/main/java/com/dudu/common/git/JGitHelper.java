package com.dudu.common.git;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.*;

/**
 * 〈一句话功能简述〉<br> 
 * 封装了部分源码方法, 方便调用
 *
 * @author du'jian'wei
 * @date 2019/7/15
 */
public final class JGitHelper {

    private JGit git;

    /**
     * 通过构造器初始化jGit实例
     *
     * @param gitUrl git仓库路径
     * @param username 用户名
     * @param password 密码
     * @param localPath 本地路径
     */
    public JGitHelper(String gitUrl,
                      String username,
                      String password,
                      String localPath) {
        this.git = new JGit(gitUrl, username, password, localPath);
    }

    /**
     * 获取分支增量方法
     *
     * @param localBranch
     *                  基础分支
     * @param remoteBranch
     *                  比对分支
     * @param isDelete
     *                  是否删除存储库
     *                  <code>true</code>, 使用完后删除该存储库
     *
     * @return 分支增量方法集合
     */
    private Map<String, Map<String, String>> getBranchIncremental(String localBranch, String remoteBranch, boolean isDelete)
            throws GitAPIException, IOException {

        String localRepository = git.createRepository(remoteBranch);

        List<JGitBean> beanList = git.diffBranch(localBranch, remoteBranch);

        Map<String, Map<String, String>> incrementalClass = JavaParserHelper.matchMethod(beanList, localRepository);

        if (isDelete) {
            git.delRepository();
        }

        return incrementalClass;
    }

    /**
     * 获取分支增量方法
     *
     * @param localBranch
     *                  基础分支
     * @param remoteBranch
     *                  远程分支
     * @param isDelete
     *                  是否删除存储库
     *                  <code>true</code>, 使用完后删除该存储库
     *
     * @return 分支增量方法集合
     */
    public Map<String, Map<String, String>> getBranchIncrementalMethod(String localBranch, String remoteBranch, boolean isDelete) {
        try {
            return getBranchIncremental(localBranch, remoteBranch, isDelete);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }

    /**
     * 获取tag增量方法
     *
     * @param remoteTag1
     *                  远程tag1名称
     * @param remoteTag2
     *                  远程tag2名称
     * @param isDelete
     *                  是否删除存储库
     *                  <code>true</code>, 使用完后删除该存储库
     *
     * @return tag增量方法集合
     */
    private Map<String, Map<String, String>> getTagIncremental(String remoteTag1, String remoteTag2, boolean isDelete)
            throws GitAPIException, IOException {

        String localRepository = git.createRepository("master");

        List<JGitBean> beanList = git.diffTag(remoteTag1, remoteTag2);

        Map<String, Map<String, String>> incrementalClass = JavaParserHelper.matchMethod(beanList, localRepository);

        if (isDelete) {
            git.delRepository();
        }

        return incrementalClass;
    }

    /**
     * 获取tag增量方法
     *
     * @param remoteTag1
     *                  远程tag1名称
     * @param remoteTag2
     *                  远程tag2名称
     * @param isDelete
     *                  是否删除存储库
     *                  <code>true</code>, 使用完后删除该存储库
     *
     * @return tag增量方法集合
     */
    public Map<String, Map<String, String>> getTagIncrementalMethod(String remoteTag1, String remoteTag2, boolean isDelete) {
        try {
            return getTagIncremental(remoteTag1, remoteTag2, isDelete);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }

    public static void main(String[] args) {

        String gitUrl = "https://github.com/1582871549/demo.git";
        String username = "1582871549@qq.com";
        String password = "840742807du";
        String localPath = "D:\\aaa";

        String localBranch = "dev";
        String remoteBranch = "dev-shiro";

        String a = "v.1.0";
        String b = "v.1.1";

        JGitHelper helper = new JGitHelper(gitUrl, username, password, localPath);

        Map<String, Map<String, String>> classMap = helper.getBranchIncrementalMethod(localBranch, remoteBranch, true);
        // Map<String, List<String>> classMap = helper.getTagIncrementalMethod(a, b, true);

        for (Map.Entry<String, Map<String, String>> classEntry : classMap.entrySet()) {
            System.out.println("=========   " + classEntry.getKey() + "   =========");
            for (Map.Entry<String, String> methodEntry : classEntry.getValue().entrySet()) {
                System.out.println("---------   " + methodEntry.getKey() + "   ---------");
            }
        }

    }


}
