package com.dudu.common.git;

import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.service.coverage.CodeDiffGetStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
@Slf4j
public final class JGitHelper {

    /**
     * 打开指定位置中的git存储库
     * 如果目录不存在
     * 不会抛出异常, 也不会输出任何存储库信息
     */
    private static Repository openRepository(String projectPath) throws IOException {
        return new FileRepositoryBuilder()
                .setGitDir(new File(projectPath, ".git"))
                .build();
    }

    public static void cloneRepository(JGitBO jGitBO) throws IOException, GitAPIException {

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

    public static void checkoutLocalBranch(JGitBO jGitBO) throws IOException, GitAPIException {

        String projectPath = jGitBO.getProjectPath();
        String compare = jGitBO.getCompare();

        try (Repository repository = JGitHelper.openRepository(projectPath)) {
            try (Git git = new Git(repository)) {
                git.checkout()
                        .setName("refs/heads/" + compare)
                        .call();
            }
        }
    }

    public static Map<String, List<DiffClassBO>> compareCodeDiff(CodeDiffGetStrategy codeDiffGetStrategy, JGitBO jGitBO) {

        try {
            return compareDiff(codeDiffGetStrategy, jGitBO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, List<DiffClassBO>> compareDiff(CodeDiffGetStrategy codeDiffGetStrategy, JGitBO jGitBO) throws IOException{

        String projectPath = jGitBO.getProjectPath();
        String base = jGitBO.getBase();
        String compare = jGitBO.getCompare();

        try (Repository repository = JGitHelper.openRepository(projectPath)) {

            List<DiffEntry> diffEntrys = codeDiffGetStrategy.getCodeDiff(repository, base, compare);

            return getDiffClassBO(repository, diffEntrys);
        }
    }

    private static Map<String, List<DiffClassBO>> getDiffClassBO(Repository repository,
                                                                 List<DiffEntry> diffEntryList) throws IOException {

        Map<String, List<DiffClassBO>> diffClassBOMap = new HashMap<>(16);

        try (DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream())) {

            formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            formatter.setRepository(repository);

            for (DiffEntry diff : diffEntryList) {

                formatter.format(diff);
                DiffEntry.ChangeType changeType = diff.getChangeType();

                if (changeType == DiffEntry.ChangeType.ADD || changeType == DiffEntry.ChangeType.MODIFY) {

                    String classPath = diff.getNewPath();

                    List<DiffClassBO> diffClassBOS = new ArrayList<>(30);

                    for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                        for (Edit edit : hunk.toEditList()) {

                            Edit.Type type = edit.getType();

                            if (type == Edit.Type.INSERT || type == Edit.Type.REPLACE) {

                                int beginB = edit.getBeginB();
                                int endB = edit.getEndB();
                                // 实际差异开始行
                                int realBeginB = beginB + 1;

                                DiffClassBO diffClassBO = new DiffClassBO(realBeginB, endB);

                                diffClassBOS.add(diffClassBO);
                            }

                            if (type == Edit.Type.DELETE) {

                                int beginB = edit.getBeginB();
                                int endB = edit.getEndB();
                                // 实际差异开始行
                                int realBeginB = beginB + 1;

                                if (beginB == endB) {

                                    DiffClassBO diffClassBO = new DiffClassBO(realBeginB, realBeginB);
                                    diffClassBOS.add(diffClassBO);
                                } else {

                                    DiffClassBO diffClassBO = new DiffClassBO(realBeginB, endB);
                                    diffClassBOS.add(diffClassBO);
                                }
                            }
                        }
                    }
                    diffClassBOMap.put(classPath, diffClassBOS);
                }
            }
        }
        return diffClassBOMap;
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
