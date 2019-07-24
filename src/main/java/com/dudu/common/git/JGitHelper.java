package com.dudu.common.git;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 封装了部分源码方法, 方便调用
 *
 * @author du'jian'wei
 * @date 2019/7/15
 */
public final class JGitHelper {

    private JGit git;
    private JavaParserHelper parser;

    /**
     * 通过构造器初始化jGit实例
     *
     * @param gitUrl git仓库路径
     * @param branchName 分支名称
     * @param username 用户名
     * @param password 密码
     * @param repositoryDirName 本地仓库名称
     */
    public JGitHelper(String gitUrl, String branchName, String username, String password, String repositoryDirName) {
        this.git = new JGit(gitUrl, branchName, username, password, repositoryDirName);
    }

    /**
     * 获取增量方法
     *  @param remoteBranch
     *                  远程分支
     * @param isDelete
     *                  是否删除存储库
     */
    private void getIncremental(String remoteBranch, boolean isDelete) throws GitAPIException, IOException {

        String localRepository = git.cloneRepository();

        List<JGitBean> beanList = git.diffBranch(remoteBranch);

        this.parser = new JavaParserHelper(localRepository);

        this.parser.matchMethod(beanList);

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
    public Map<String, Map<String, List<String>>> getIncrementalMethod(String remoteBranch, boolean isDelete) {
        try {
            getIncremental(remoteBranch, isDelete);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return parser.getIncrementalClass();
    }

    public static void main(String[] args) {
        String gitUrl = "https://github.com/1582871549/jacoco.git";
        String branchName = "master";
        String username = "1582871549@qq.com";
        String password = "840742807du";
        String repositoryDirName = "gitRepository";

        String remoteBranch = "dev";

        JGitHelper helper = new JGitHelper(gitUrl, branchName, username, password, repositoryDirName);

        Map<String, Map<String, List<String>>> classMap = helper.getIncrementalMethod(remoteBranch, true);

        List<String> classList = new ArrayList<>(11);

        Map<String, List<String>> methodMap = new HashMap<>(16);

        for (Map.Entry<String, Map<String, List<String>>> classEntry : classMap.entrySet()) {

            String className = classEntry.getKey();
            classList.add(className);

            for (Map.Entry<String, List<String>> methodEntry : classEntry.getValue().entrySet()) {

                String methodName = methodEntry.getKey();
                List<String> lineInfo = methodEntry.getValue();

                methodMap.put(className + "/" +methodName, lineInfo);
            }
        }

        System.out.println("=========================================");
        System.out.println();
        System.out.println();
        System.out.println();

        for (String s : classList) {
            System.out.println(s);
        }

        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println();

        for (Map.Entry<String, List<String>> entry : methodMap.entrySet()) {
            System.out.println(entry);
        }

    }


}
