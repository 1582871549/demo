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

        Map<String, List<Integer>> branchDiffMap = new HashMap<>(16);

        try (Repository repository = JGitHelper.openRepository(projectPath)) {
            try (Git git = new Git(repository)) {

                String baseBranchName = "refs/heads/" + baseBranch;
                String compareBranchName = "refs/heads/" + compareBranch;

                // 确保基础分支在本地可见
                if(repository.exactRef(baseBranchName) == null) {
                    git.branchCreate().setName(baseBranch).setStartPoint("origin/" + baseBranch).call();
                }
                // 确保比对分支在本地可见
                if(repository.exactRef(compareBranchName) == null) {
                    git.branchCreate().setName(compareBranch).setStartPoint("origin/" + compareBranch).call();
                }

                // diff结构树
                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, baseBranchName);
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, compareBranchName);

                // 对比.java后缀的文件
                List<DiffEntry> diffList = git.diff()
                        .setOldTree(oldTreeParser)
                        .setNewTree(newTreeParser)
                        .setPathFilter(PathSuffixFilter.create(".java"))
                        .call();

                try (DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream())) {

                    // 设置比较器 忽略全部空白字符
                    formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
                    formatter.setRepository(repository);

                    // 每一个diffEntry都是版本之间文件的变动差异
                    for (DiffEntry diff : diffList) {

                        formatter.format(diff);

                        // 只针对新增、修改文件
                        if (diff.getChangeType() == DiffEntry.ChangeType.ADD || diff.getChangeType() == DiffEntry.ChangeType.MODIFY) {

                            // 远程分支中的差异类路径
                            String newPath = diff.getNewPath();
                            // 存放差异行信息
                            List<Integer> lines = new ArrayList<>();

                            branchDiffMap.put(newPath, lines);

                            System.out.println("newpath  ： " + newPath);

                            //获取文件差异位置
                            for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                                for (Edit edit : hunk.toEditList()) {

                                    System.out.println("edit  ： " + edit.getType() + "   EndA " + edit.getEndA() + "   EndB " + edit.getEndB());
                                    // 状态只有为替换或新增时才去记录 (增量)
                                    if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {

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


    /**
     * tag比对
     *
     * @param remoteTag1 tag1
     * @param remoteTag2 tag2
     * @throws IOException
     * @throws GitAPIException
     */
    public Map<String, List<Integer>> diffTag(String projectPath, String remoteTag1, String remoteTag2) throws IOException, GitAPIException {

        Map<String, List<Integer>> insertMap = new HashMap<>(16);

        try (Repository repository = JGitHelper.openRepository(projectPath)) {
            try (Git git = new Git(repository)) {

                remoteTag1 = "refs/tags/" + remoteTag1;
                remoteTag2 = "refs/tags/" + remoteTag2;

                List<ObjectId> ids = new ArrayList<>();

                for (Ref ref : git.tagList().call()) {
                    if (remoteTag1.equals(ref.getName())) {
                        ids.add(0, ref.getObjectId());
                    }
                    if (remoteTag2.equals(ref.getName())) {
                        ids.add(1, ref.getObjectId());
                    }
                }

                if (ids.size() != 2) {
                    return new HashMap<>(0);
                }

                try (RevWalk walk = new RevWalk(repository)) {
                    RevCommit commit1 = walk.parseCommit(ids.get(0));
                    RevCommit commit2 = walk.parseCommit(ids.get(1));

                    // diff结构树
                    AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, commit1);
                    AbstractTreeIterator newTreeParser = prepareTreeParser(repository, commit2);

                    // 对比.java后缀的文件
                    List<DiffEntry> diffList = git.diff()
                            .setOldTree(oldTreeParser)
                            .setNewTree(newTreeParser)
                            .setPathFilter(PathSuffixFilter.create(".java"))
                            .call();

                    try (DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream())) {

                        // 设置比较器 忽略全部空白字符
                        formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
                        formatter.setRepository(repository);

                        // 每一个diffEntry都是第个文件版本之间的变动差异
                        for (DiffEntry diff : diffList) {

                            //打印文件差异具体内容
                            formatter.format(diff);

                            // 只针对新增、修改文件
                            if (diff.getChangeType() == DiffEntry.ChangeType.ADD || diff.getChangeType() == DiffEntry.ChangeType.MODIFY) {

                                // 远程分支中的差异类路径
                                String newPath = diff.getNewPath();
                                // 存放差异行信息
                                List<Integer> lines = new ArrayList<>();

                                insertMap.put(newPath, lines);

                                //获取文件差异位置
                                for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                                    for (Edit edit : hunk.toEditList()) {
                                        // 状态只有为替换或新增时才去记录 (增量)
                                        if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {
                                            if (edit.getEndA() != 0) {
                                                // 记录远程分支中的每个差异代码块的结束行, 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                                lines.add(edit.getEndB());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    walk.dispose();
                }
            }
        }
        return insertMap;
    }


    private AbstractTreeIterator prepareTreeParser(Repository repository, RevCommit objectId) throws IOException {
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

    /**
     * 删除存储库
     */
    public static void deleteDirectory(String repositoryPath) throws IOException {
        FileUtils.deleteDirectory(new File(repositoryPath));
    }

}
