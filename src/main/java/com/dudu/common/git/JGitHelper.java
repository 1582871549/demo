package com.dudu.common.git;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 封装了部分源码方法, 方便调用
 *
 * @author du'jian'wei
 * @date 2019/7/15
 */
public final class JGitHelper {

    private JGit git;

    public JGitHelper(String gitUrl, String branchName, String username, String password, String repositoryDirName) {
        this.git = JGit.getInstance(gitUrl, branchName, username, password, repositoryDirName);
    }

    /**
     * 获取增量方法
     *
     * @param remoteBranch
     *                  远程分支
     * @param isDelete
     *                  是否删除存储库
     *                  <code>true</code>, 使用完后删除该存储库
     */
    private void getIncremental(String remoteBranch, boolean isDelete) throws GitAPIException, IOException {
        git.cloneRepository();
        List<JGitBean> beanList = git.diffBranch(remoteBranch);
        if (isDelete) {
            git.delRepository();
        }
    }

    /**
     * 获取增量方法
     *
     * @param remoteBranch
     *                  远程分支
     * @param isDelete
     *                  是否删除存储库
     *                  <code>true</code>, 使用完后删除该存储库
     */
    public void getIncrementalMethod(String remoteBranch, boolean isDelete) {
        try {
            getIncremental(remoteBranch, isDelete);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取增量方法
     *
     * @param remoteBranch
     *                  远程分支
     */
    public void getIncrementalMethod(String remoteBranch) {
        try {
            getIncremental(remoteBranch, true);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws GitAPIException, IOException {
        String gitUrl = "https://github.com/1582871549/jacoco.git";
        String branchName = "master";
        String username = "1582871549@qq.com";
        String password = "840742807du";
        String repositoryDirName = "gitRepository";

        String remoteBranch = "dev";

        JGitHelper helper = new JGitHelper(gitUrl, branchName, username, password, repositoryDirName);

        helper.getIncrementalMethod(remoteBranch, true);

        // List<JGitBean> beanList = git.diffBranch(remoteBranch);
        //
        // for (JGitBean bean : beanList) {
        //     System.out.println(bean);
        // }
    }


}
