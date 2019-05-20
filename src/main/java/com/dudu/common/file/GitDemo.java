/**
 * FileName: GitDemo
 * Author:   大橙子
 * Date:     2019/4/10 16:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/4/10
 * @since 1.0.0
 */
@Slf4j
public class GitDemo {

    public static void main(String[] args) {
        // String gitPath = "https://github.com/1582871549/demo.git";
        // String branchName = "master";
        // String username = "1582871549@qq.com";
        // String password = "840742807du";
        // String workSpace = "D:\\Soft_Package\\gitRepo";
        //
        // boolean b = new GitDemo().cloneRepository(gitPath, branchName, username, password, workSpace);

    }

    /**
     * 生成覆盖率报告
     * 生成覆盖率报告的前提是需要有被测服务的源码和编译后的class文件，故会先下载被测服务的源码，然后编译
     *
     * 获取被测服务的源码
     * @param gitPath git
     * @param branchName 分支名称
     * @param workSpace 存储位置
     * @return
     */
    private boolean cloneRepository(String gitPath, String branchName, String username, String password, String workSpace) {

        boolean success = true;

        log.info("clone " + gitPath + "_" + branchName + " to " + workSpace);

        //1. 克隆代码
        try {
            Git.cloneRepository()
                    .setURI(gitPath)
                    .setBranch(branchName)
                    .setDirectory(new File(workSpace))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call();
        } catch (Exception e) {
            success = false;
            log.error("clone " + gitPath + "_" + branchName + " to " + workSpace + "错误", e);
        }
        log.info("clone success");
        return success;
    }

    /**
     * 编译clone到的源码项目, 生成class文件。
     */
    public void aa(){

    }


}
