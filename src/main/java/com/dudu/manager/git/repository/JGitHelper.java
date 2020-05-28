package com.dudu.manager.git.repository;

import com.dudu.manager.git.entity.JGitBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 封装了部分源码方法, 方便调用
 *
 * @author du'jian'wei
 * @date 2019/7/15
 */
@Slf4j
public final class JGitHelper {

    /**
     * 打开指定位置中的git存储库
     * 如果目录不存在
     * 不会抛出异常, 也不会输出任何存储库信息
     */
    public static Repository openRepository(String projectPath) {
        try {
            return openLocalRepository(projectPath);
        } catch (IOException e) {
            // throw new BusinessException(e);
            throw new RuntimeException();
        }
    }

    private static Repository openLocalRepository(String projectPath) throws IOException {
        return new FileRepositoryBuilder()
                .setGitDir(new File(projectPath, ".git"))
                .build();
    }

    public static void createRepository(JGitBO jGitBO) {
        try {
            cloneRepository(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException();
        }
    }

    private static void cloneRepository(JGitBO jGitBO) throws IOException, GitAPIException {

        String url = jGitBO.getUrl();
        String username = jGitBO.getUsername();
        String password = jGitBO.getPassword();
        String defaultBranch = jGitBO.getDefaultBranch();
        String projectPath = jGitBO.getProjectPath();


        File projectDir = new File(projectPath);

        log.info("projectDir : " + projectDir);

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if (projectDir.exists()) {
            FileUtils.deleteDirectory(projectDir);
        }

        mkdirsDirectory(projectDir);

        try (Git ignored = Git.cloneRepository()
                .setURI(url)
                .setBranch(defaultBranch)
                .setDirectory(projectDir)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()) {

            log.info("Cloning from " + url + " to " + projectPath + ", branch : " + defaultBranch);
        }
    }

    public static void checkoutBranch(JGitBO jGitBO) {
        try {
            checkoutLocalBranch(jGitBO);
        } catch (GitAPIException e) {
            // throw new BusinessException("checkout Local branch failed", e);
            throw new RuntimeException();
        }
    }

    private static void checkoutLocalBranch(JGitBO jGitBO) throws GitAPIException {

        String projectPath = jGitBO.getProjectPath();
        String compare = jGitBO.getCompare();

        try (Repository repository = openRepository(projectPath)) {
            try (Git git = new Git(repository)) {
                git.checkout()
                        .setName("refs/heads/" + compare)
                        .call();
            }
        }
    }


    /**
     * 创建多级目录
     */
    public static void mkdirsDirectory(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("创建git存储库多级目录失败");
            }
        }
    }

    public static void mkdirsDirectory(File file) throws IOException {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("创建git存储库多级目录失败");
            }
        }
    }

}
