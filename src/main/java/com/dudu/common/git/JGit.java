/**
 * FileName: JGit
 * Author:   dujianwei
 * Date:     2019/7/16 9:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br> 
 * jgit操作类
 *
 * @author dujianwei
 * @create 2019/7/16
 * @since 1.0.0
 */
public final class JGit {

    private static final String REPO_PRE = "repository";

    private final String gitUrl;
    private final String username;
    private final String password;
    private final String localPath;
    private final List<JGitBean> gitBeanList;

    private File repository;

    /**
     * 创建一个JGit实例
     *
     * @param gitUrl git仓库路径
     * @param username 用户名
     * @param password 密码
     * @param localPath 本地路径
     */
    public JGit(String gitUrl, String username, String password, String localPath) {
        this.gitUrl = gitUrl;
        this.username = username;
        this.password = password;
        this.localPath = localPath;
        this.gitBeanList = new ArrayList<>(11);
    }

    /**
     * 创建git存储库文件夹
     *
     * @param branch 分支名称
     * @return 返回当前存储库的路径
     */
    public String createRepository(String branch) throws IOException {

        String[] split = Pattern.compile("-").split(UUID.randomUUID().toString());

        // 拼接一个随机文件名
        final String repositoryName = REPO_PRE + split[0];

        // 为克隆的存储库准备一个文件夹
        repository = new File(localPath, repositoryName);

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if (repository.exists()) {
            if(!repository.delete()) {
                throw new IOException("Could not delete localRepository folder : " + repository.getPath());
            }
        }

        System.out.println("Cloning from " + gitUrl + " to " + localPath + ", branch : " + branch);

        cloneRepository(branch);

        return repository.getPath();
    }

    /**
     * 克隆分支代码
     *
     * @param branch 分支名称
     */
    private void cloneRepository(String branch) {
        // 这样的写法系统会在内部自动关流
        try (Git ignored = Git.cloneRepository()
                .setURI(gitUrl)
                .setBranch(branch)
                .setDirectory(repository)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()) {
        } catch (GitAPIException e) {
            e.printStackTrace();
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
                .setGitDir(new File(repository, ".git"))
                .build();
    }

    /**
     * 分支比对
     *
     * <p>
     *     拉取【远程分支】与【克隆存储库时使用的分支】进行对比
     * </p>
     *
     * @param localBranch 基础分支
     * @param remoteBranch 比对分支
     */
    public List<JGitBean> diffBranch(String localBranch, String remoteBranch) throws IOException, GitAPIException {

        try (Repository repository = openRepository()) {
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
                            // 每一个差异类对应一个bean实例
                            JGitBean bean = new JGitBean(newPath, lines);

                            gitBeanList.add(bean);

                            //获取文件差异位置
                            for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                                for (Edit edit : hunk.toEditList()) {

                                    // 状态只有为替换或新增时才去记录 (增量)
                                    if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {

                                        // 记录远程分支中的每个差异代码块的结束行, 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                        lines.add(edit.getEndB());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return gitBeanList;
    }

    /**
     * tag比对
     *
     * @param remoteTag1 tag1
     * @param remoteTag2 tag2
     * @throws IOException
     * @throws GitAPIException
     */
    public List<JGitBean> diffTag(String remoteTag1, String remoteTag2) throws IOException, GitAPIException {
        try (Repository repository = openRepository()) {
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
                    return new ArrayList<>();
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
                                // 每一个差异类对应一个bean实例
                                JGitBean bean = new JGitBean(newPath, lines);

                                gitBeanList.add(bean);

                                //获取文件差异位置
                                for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                                    for (Edit edit : hunk.toEditList()) {

                                        // 状态只有为替换或新增时才去记录 (增量)
                                        if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {

                                            // 记录远程分支中的每个差异代码块的结束行, 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                            lines.add(edit.getEndB());
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
        return gitBeanList;
    }

    /**
     * 列出Git存储库中的本地分支
     *
     * @return 本地分支
     */
    public List<Ref> listLocalBranch() throws IOException, GitAPIException {
        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {
                return git.branchList().call();
            }
        }
    }

    /**
     * 列出Git存储库中的远程分支
     *
     * @return 远程分支
     */
    public List<Ref> listRemoteBranch() throws IOException, GitAPIException {
        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {
                return git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
            }
        }
    }

    /**
     * 列出Git存储库中的所有Tag
     *
     * @throws IOException
     * @throws GitAPIException
     */
    public List<Ref> ListTag() throws IOException, GitAPIException {
        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {
                return git.tagList().call();
            }
        }
    }

    /**
     * 官方提供的方法, 请勿修改
     *
     * @param repository 存储库实例
     * @param branchName 分支名称
     * @return 返回git生成的结构树
     */
    private AbstractTreeIterator prepareTreeParser(Repository repository, String branchName) throws IOException {
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
     * 删除存储库
     */
    public void delRepository() throws IOException {
        FileUtils.deleteDirectory(repository);
    }
}
