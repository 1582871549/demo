package com.dudu.common.git;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public static void cloneRepository(String url,
                                       String username,
                                       String password,
                                       String compareBranch,
                                       String projectPath) throws IOException, GitAPIException {

        File projectDir = new File(projectPath);

        log.info("projectDir : " + projectDir);

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if (projectDir.exists()) {
            FileUtils.deleteDirectory(projectDir);
        }

        JGitHelper.mkdirsDirectory(projectDir);

        try (Git ignored = Git.cloneRepository()
                .setURI(url)
                .setBranch(compareBranch)
                .setDirectory(projectDir)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()) {

            log.info("Cloning from " + url + " to " + projectPath + ", branch : " + compareBranch);
        }
    }

    public static void checkoutLocalBranch(String projectPath, String branchName) throws IOException, GitAPIException {
        try (Repository repository = JGitHelper.openRepository(projectPath)) {
            try (Git git = new Git(repository)) {
                git.checkout()
                        .setName("refs/heads/" + branchName)
                        .call();
            }
        }
    }

    public static Map<String, List<Integer>> compareBranchDiff(String projectPath,
                                                               String baseBranch,
                                                               String compareBranch) throws IOException, GitAPIException {

        try (Repository repository = JGitHelper.openRepository(projectPath)) {

            List<DiffEntry> diffEntryList = getDiffAndCreateBranchPoint(repository, baseBranch, compareBranch);

            return getDiffMap(repository, diffEntryList);
        }
    }

    public static Map<String, List<Integer>> compareTagDiff(String projectPath,
                                                     String baseTag,
                                                     String compareTag) throws IOException, GitAPIException {

        try (Repository repository = JGitHelper.openRepository(projectPath)) {

            List<DiffEntry> diffEntryList = getDiffAndCreateTagPoint(repository, baseTag, compareTag);

            return getDiffMap(repository, diffEntryList);
        }
    }

    private static List<DiffEntry> getDiffAndCreateTagPoint(Repository repository,
                                                     String baseTag,
                                                     String compareTag) throws GitAPIException, IOException {

        String baseTagName = "refs/tags/" + baseTag;
        String compareTagName = "refs/tags/" + compareTag;

        Map<String, ObjectId> tagMap = new HashMap<>();

        try (Git git = new Git(repository)) {

            for (Ref ref : git.tagList().call()) {
                if (baseTagName.equals(ref.getName())) {
                    tagMap.put("baseTag", ref.getObjectId());
                }
                if (compareTagName.equals(ref.getName())) {
                    tagMap.put("compareTag", ref.getObjectId());
                }
            }

            if (tagMap.size() != 2) {
                return Collections.emptyList();
            }

            try (RevWalk walk = new RevWalk(repository)) {

                RevCommit baseTagCommit = walk.parseCommit(tagMap.get("baseTag"));
                RevCommit compareTagCommit = walk.parseCommit(tagMap.get("compareTag"));

                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, baseTagCommit);
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, compareTagCommit);

                return git.diff()
                        .setOldTree(oldTreeParser)
                        .setNewTree(newTreeParser)
                        .setPathFilter(PathSuffixFilter.create(".java"))
                        .call();
            }
        }
    }

    private static List<DiffEntry> getDiffAndCreateBranchPoint(Repository repository,
                                                               String baseBranch,
                                                               String compareBranch) throws IOException, GitAPIException {

        String baseBranchName = "refs/heads/" + baseBranch;
        String compareBranchName = "refs/heads/" + compareBranch;

        try (Git git = new Git(repository)) {

            if(repository.exactRef(baseBranchName) == null) {
                git.branchCreate().setName(baseBranch).setStartPoint("origin/" + baseBranch).call();
            }

            if(repository.exactRef(compareBranchName) == null) {
                git.branchCreate().setName(compareBranch).setStartPoint("origin/" + compareBranch).call();
            }

            AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, baseBranchName);
            AbstractTreeIterator newTreeParser = prepareTreeParser(repository, compareBranchName);

            return git.diff()
                    .setOldTree(oldTreeParser)
                    .setNewTree(newTreeParser)
                    .setPathFilter(PathSuffixFilter.create(".java"))
                    .call();
        }
    }

    private static Map<String, List<Integer>> getDiffMap(Repository repository,
                                                         List<DiffEntry> diffEntryList) throws IOException {

        Map<String, List<Integer>> branchDiffMap = new HashMap<>(16);

        try (DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream())) {

            formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            formatter.setRepository(repository);

            for (DiffEntry diff : diffEntryList) {

                formatter.format(diff);

                DiffEntry.ChangeType changeType = diff.getChangeType();

                if (changeType == DiffEntry.ChangeType.ADD
                        || changeType == DiffEntry.ChangeType.MODIFY
                        || changeType == DiffEntry.ChangeType.DELETE) {

                    // 远程分支中的差异类路径
                    String newPath = diff.getNewPath();
                    // 存放差异行信息
                    List<Integer> lines = new ArrayList<>();

                    branchDiffMap.put(newPath, lines);

                    System.out.println("newpath  ： " + newPath);

                    for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                        for (Edit edit : hunk.toEditList()) {

                            System.out.println("edit  ： " + edit.getType() + "   EndA " + edit.getEndA() + "   EndB " + edit.getEndB());

                            Edit.Type type = edit.getType();

                            if (type == Edit.Type.INSERT || type == Edit.Type.REPLACE || type == Edit.Type.DELETE) {

                                // 当endA == 0 的时候， 说明当前类型为新增类。 在匹配差异方法时添加全部方法
                                if (edit.getEndA() != 0) {
                                    // 记录远程分支中的每个差异代码块的结束行, 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                    lines.add(edit.getEndB());
                                    System.out.println("====================================edit.getEndB() " + edit.getEndB());
                                }
                            }
                        }
                    }
                }
            }
        }
        return branchDiffMap;
    }

    /**
     * 官方提供的方法, 请勿修改
     *
     * @param repository 存储库实例
     * @param branchName 分支名称
     * @return 返回git生成的结构树
     */
    private static AbstractTreeIterator prepareTreeParser(Repository repository, String branchName) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        Ref branchRef = repository.exactRef(branchName);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(branchRef.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, RevCommit objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(objectId);
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
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
