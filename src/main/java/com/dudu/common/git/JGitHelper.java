package com.dudu.common.git;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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
public final class JGitHelper {

    /**
     * 打开指定位置中的git存储库
     * <p>
     *     如果目录不存在,不会抛出异常. 也不会输出任何存储库信息
     * </p>
     *
     * @param repositoryPath 存储库路径
     * @return 存储库实例
     */
    public static Repository openRepository(String repositoryPath) throws IOException {
        return new FileRepositoryBuilder()
                .setGitDir(new File(repositoryPath, ".git"))
                .build();
    }

    /**
     * 删除存储库
     */
    public static void deleteDirectory(String repositoryPath) throws IOException {
        FileUtils.deleteDirectory(new File(repositoryPath));
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
     * 获取分支增量方法
     *
     * @param gitBean
     *                  bean
     * @param isDelete
     *                  是否删除存储库
     *
     * @return 分支增量方法集合
     */
    public static Map<String, Map<String, String>> getBranchIncremental(JGitBean gitBean, boolean isDelete) throws IOException, GitAPIException {

        String repositoryPath = gitBean.getRepositoryPath();

        // 拉取本地分支创建存储库

        // 比对本地分支与远程分支
        Map<String, List<Integer>> insertMap = getBranchDiff("", "", "");

        for (Map.Entry<String, List<Integer>> entry : insertMap.entrySet()) {
            System.out.println(entry);
        }

        // 切换远程分支
        JGit.checkout(repositoryPath, gitBean.getRemoteBranch());

        // 增量方法
        Map<String, Map<String, String>> incrementalClass = JavaParserHelper.matchMethod(insertMap, repositoryPath);

        if (isDelete) {
            deleteDirectory(repositoryPath);
        }

        return incrementalClass;
    }

    public static void main(String[] args) throws IOException, GitAPIException {

        String path = "D:\\aaa\\ccc";
        String gitUrl = "https://github.com/1582871549/demo.git";
        String username = "1582871549@qq.com";
        String password = "840742807du";

        String localBranch = "dev";
        String remoteBranch = "dev-shiro";

        String repositoryPath = "D:\\aaa\\ccc\\repository";

        JGitBean bean = new JGitBean(path, gitUrl, username, password, localBranch, remoteBranch, repositoryPath);

        getIncremental(bean, false);

        // getTagIncremental(bean, false);

        // aaa(bean);

        // bbb(bean);
    }

    public static void getIncremental(JGitBean bean, boolean isDelete) throws IOException, GitAPIException {

        Map<String, Map<String, String>> classMap = JGitHelper.getBranchIncremental(bean, isDelete);

        for (Map.Entry<String, Map<String, String>> classEntry : classMap.entrySet()) {
            for (Map.Entry<String, String> methodEntry : classEntry.getValue().entrySet()) {
                System.out.println("key : " + classEntry.getKey() + "   value : " + methodEntry.getKey());
            }
        }

    }

    public static void getTagIncremental(JGitBean bean, boolean isDelete) throws IOException, GitAPIException {

        Map<String, Map<String, String>> classMap = JGitHelper.getBranchIncremental(bean, isDelete);

        for (Map.Entry<String, Map<String, String>> classEntry : classMap.entrySet()) {
            for (Map.Entry<String, String> methodEntry : classEntry.getValue().entrySet()) {
                System.out.println("key : " + classEntry.getKey() + "   value : " + methodEntry.getKey());
            }
        }

    }

    public static void aaa(JGitBean bean) throws IOException, GitAPIException {

        try (Repository repository = JGitHelper.openRepository(bean.getRepositoryPath())) {
            try (Git git = new Git(repository)) {

                // 遍历存储库所有tag, 获取tag对应的commitId.
                for (Ref ref : git.tagList().call()) {
                    System.out.println("name : " + ref.getName() + ", id : " + ref.getObjectId());

                    try (RevWalk walk = new RevWalk(repository)) {
                        RevCommit commit2 = walk.parseCommit(ref.getObjectId());

                        git.branchCreate().setName("tagA").setStartPoint(commit2).call();
                    }

                }


                // 根据tag commitId 来创建分支tagA, 完成本地分支创建


                // 切换到tagA分支, 并编译tagA分支代码.

                List<Ref> list = git.branchList().call();

                System.out.println("------------------");

                for (Ref ref : list) {
                    System.out.println(ref.getName());
                }
            }
        }

    }

    public static void bbb(JGitBean bean) throws IOException, GitAPIException {

        // 切换远程分支
        // JGit.checkout(bean.getRepositoryPath(), "refs/heads/dev");
        // JGit.checkout(bean.getRepositoryPath(), "dev-shiro");
        JGit.checkout(bean.getRepositoryPath(), "tagA");
    }

    public static Map<String, List<Integer>> getBranchDiff(String projectPath, String localBranch, String remoteBranch) throws IOException, GitAPIException {

        Map<String, List<Integer>> insertMap = new HashMap<>(16);

        try (Repository repository = JGitHelper.openRepository(projectPath)) {
            try (Git git = new Git(repository)) {

                String localBranchName = "refs/heads/" + localBranch;
                String remoteBranchName = "refs/heads/" + remoteBranch;

                // 确保基础分支在本地可见
                if(repository.exactRef(localBranchName) == null) {
                    git.branchCreate().setName(localBranch).setStartPoint("origin/" + localBranch).call();
                }
                // 确保比对分支在本地可见
                if(repository.exactRef(remoteBranchName) == null) {
                    git.branchCreate().setName(remoteBranch).setStartPoint("origin/" + remoteBranch).call();
                }

                // diff结构树
                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, localBranchName);
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, remoteBranchName);

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

                    System.out.println();
                    System.out.println();
                    System.out.println();

                    // 每一个diffEntry都是版本之间文件的变动差异
                    for (DiffEntry diff : diffList) {

                        formatter.format(diff);

                        // 控制台打印差异文件信息
                        // System.out.println(System.out);
                        // System.out.println("Diff: " + diff.getChangeType() + ": " + (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));

                        // 只针对新增、修改文件
                        if (diff.getChangeType() == DiffEntry.ChangeType.ADD || diff.getChangeType() == DiffEntry.ChangeType.MODIFY) {

                            // 远程分支中的差异类路径
                            String newPath = diff.getNewPath();
                            // 存放差异行信息
                            List<Integer> lines = new ArrayList<>();

                            insertMap.put(newPath, lines);

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
        return insertMap;
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

}
