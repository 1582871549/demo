/**
 * FileName: JGit
 * Author:   大橙子
 * Date:     2019/7/16 9:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
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
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/7/16
 * @since 1.0.0
 */
public final class JGit {

    private final String gitUrl;
    private final String localBranch;
    private final String username;
    private final String password;
    private final String repositoryDirName;
    private String localRepository;
    private final List<JGitBean> gitBeanList;

    private static volatile JGit instance = null;

    /**
     * 创建一个JGit实例
     *
     * @param gitUrl git仓库路径
     * @param branchName 分支名称
     * @param username 用户名
     * @param password 密码
     * @param repositoryDirName 本地仓库名称
     */
    private JGit(String gitUrl, String branchName, String username, String password, String repositoryDirName) {
        this.gitUrl = gitUrl;
        this.localBranch = branchName;
        this.username = username;
        this.password = password;
        this.repositoryDirName = repositoryDirName;
        this.gitBeanList = new ArrayList<>(11);
    }

    /**
     * 双检锁单例
     *
     * @param gitUrl git仓库路径
     * @param branchName 分支名称
     * @param username 用户名
     * @param password 密码
     * @param repositoryDirName 本地仓库名称
     * @return JGit实例
     */
    public static JGit getInstance(String gitUrl, String branchName, String username, String password, String repositoryDirName) {
        if (instance == null) {
            synchronized (JGit.class) {
                if (instance == null) {
                    instance = new JGit(gitUrl, branchName, username, password, repositoryDirName);
                }
            }
        }
        return instance;
    }

    /**
     * 克隆一个git存储库
     */
    public void cloneRepository() throws GitAPIException, IOException {
        // 为克隆的存储库准备一个临时文件夹
        File tempFolder = File.createTempFile(repositoryDirName, "");

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if(!tempFolder.delete()) {
            throw new IOException("Could not delete temporary folder " + tempFolder);
        }

        System.out.println("Cloning from " + gitUrl + " to " + tempFolder + ", branch : " + localBranch);

        // 这样的写法系统会在内部自动关流
        try (Git ignored = Git.cloneRepository()
                .setURI(gitUrl)
                .setBranch(localBranch)
                .setDirectory(tempFolder)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()) {

            // 将存储库路径赋予全局变量, 方便后续删除
            this.localRepository = tempFolder.getPath();

        }
    }

    /**
     * 打开指定位置中的git存储库
     * <p>
     *     如果目录不存在,不会抛出异常. 也不会输出任何存储库信息
     * </p>
     *
     * @return 存储库实例
     */
    public Repository openRepository() throws IOException {
        return new FileRepositoryBuilder()
                .setGitDir(new File(localRepository, ".git"))
                .build();
    }

    /**
     * 分支比对
     *
     * <p>
     *     拉取【远程分支】与【克隆存储库时使用的分支】进行对比
     * </p>
     *
     * @param remoteBranch 远程分支名称
     */
    public List<JGitBean> diffBranch(String remoteBranch) throws IOException, GitAPIException {

        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {

                String localBranchName = "refs/heads/" + localBranch;
                String remoteBranchName = "refs/heads/" + remoteBranch;

                // 首先，我们需要确保远程分支在本地是可见的。
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

                    // 每一个diffEntry都是版本之间文件的变动差异
                    for (DiffEntry diff : diffList) {

                        formatter.format(diff);

                        // 控制台打印差异文件信息
                        // System.out.println(System.out);
                        // System.out.println("Diff: " + diff.getChangeType() + ": " + (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));

                        // 只针对新增、修改文件
                        if (diff.getChangeType() == DiffEntry.ChangeType.ADD || diff.getChangeType() == DiffEntry.ChangeType.MODIFY) {

                            //获取文件差异位置
                            for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {

                                // 注入自定义类
                                JGitBean bean = new JGitBean();
                                List<Integer> line = new ArrayList<>(11);
                                bean.setClassName(diff.getNewPath());
                                bean.setLine(line);

                                for (Edit edit : hunk.toEditList()) {
                                    // 状态只有为替换或新增时才去记录 (增量)
                                    if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {
                                        // 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                        line.add(edit.getEndA());
                                    }
                                }
                                gitBeanList.add(bean);
                            }
                        }
                    }
                }
            }
        }
        return gitBeanList;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            walk.dispose();
            return treeParser;
        }
    }

    /**
     * 删除存储库
     */
    public void delRepository() throws IOException {
        FileUtils.deleteDirectory(new File(localRepository));
    }
}
