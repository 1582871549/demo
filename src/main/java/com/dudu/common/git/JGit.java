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
import org.eclipse.jgit.api.LogCommand;
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

/**
 * 〈一句话功能简述〉<br> 
 * jgit操作类
 *
 * @author dujianwei
 * @create 2019/7/16
 * @since 1.0.0
 */
public final class JGit {

    private final String gitUrl;
    private final String localBranch;
    private final String username;
    private final String password;
    private final String localRepository;
    private final List<JGitBean> gitBeanList;

    private final String REPO_PRE = "repository";

    /**
     * 创建一个JGit实例
     *
     * @param gitUrl git仓库路径
     * @param branchName 分支名称
     * @param username 用户名
     * @param password 密码
     * @param localRepository 本地仓库路径
     */
    public JGit(String gitUrl, String branchName, String username, String password, String localRepository) {
        this.gitUrl = gitUrl;
        this.localBranch = branchName;
        this.username = username;
        this.password = password;
        this.localRepository = localRepository;
        this.gitBeanList = new ArrayList<>(11);
    }

    /**
     * 克隆一个git存储库
     */
    public void cloneRepository() throws GitAPIException, IOException {

        // 拼接一个随机文件名
        final String repositoryName = REPO_PRE + UUID.randomUUID().toString();

        // 为克隆的存储库准备一个文件夹
        final File repository = new File(localRepository, repositoryName);

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if(!repository.delete()) {
            throw new IOException("Could not delete localRepository folder : " + repository.getPath());
        }

        System.out.println("Cloning from " + gitUrl + " to " + repository.getPath() + ", branch : " + localBranch);

        // 这样的写法系统会在内部自动关流
        try (Git ignored = Git.cloneRepository()
                .setURI(gitUrl)
                .setBranch(localBranch)
                .setDirectory(repository)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()) {
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

                                // diff.getNewPath() 远程分支中的差异文件路径
                                JGitBean bean = new JGitBean(diff.getNewPath());
                                gitBeanList.add(bean);

                                for (Edit edit : hunk.toEditList()) {
                                    // 状态只有为替换或新增时才去记录 (增量)
                                    if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {

                                        int lineCount = edit.getEndB() - edit.getBeginB();
                                        String lineInfo = lineCount == 1 ? edit.getBeginB() + ",1" : edit.getBeginB() + "," + lineCount;
                                        // 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                        bean.getLine().put(String.valueOf(edit.getEndA()), lineInfo);
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

                String tag1Name = "refs/tags/" + remoteTag1;
                String tag2Name = "refs/tags/" + remoteTag2;

                int i = 0;
                ObjectId tag1Id = null;
                ObjectId tag2Id = null;

                for (Ref ref : git.tagList().call()) {
                    if (tag1Name.equals(ref.getName())) {
                        i++;
                        tag1Id = ref.getObjectId();
                    }
                    if (tag2Name.equals(ref.getName())) {
                        i++;
                        tag2Id = ref.getObjectId();
                    }
                }

                if (i != 2) {
                    return new ArrayList<>();
                }

                try (RevWalk walk = new RevWalk(repository)) {
                    RevCommit commit1 = walk.parseCommit(tag1Id);
                    RevCommit commit2 = walk.parseCommit(tag2Id);

                    // diff结构树
                    AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, commit1);
                    AbstractTreeIterator newTreeParser = prepareTreeParser(repository, commit2);

                    // 对比.java后缀的文件
                    List<DiffEntry> diffList = git.diff()
                            .setOldTree(oldTreeParser)
                            .setNewTree(newTreeParser)
                            .setPathFilter(PathSuffixFilter.create(".java"))
                            .call();

                    System.out.println("================================");

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

                                //获取文件差异位置
                                for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {

                                    JGitBean bean = new JGitBean(diff.getNewPath());
                                    gitBeanList.add(bean);

                                    for (Edit edit : hunk.toEditList()) {
                                        // 状态只有为替换或新增时才去记录 (增量)
                                        if (edit.getType() == Edit.Type.INSERT || edit.getType() == Edit.Type.REPLACE) {

                                            int lineCount = edit.getEndB() - edit.getBeginB();
                                            String lineInfo = lineCount == 1 ? edit.getBeginB() + ",1" : edit.getBeginB() + "," + lineCount;
                                            // 因为开始行等于实际行-1, 为避免误测我们选择以结束行来确定所属方法
                                            bean.getLine().put(String.valueOf(edit.getEndA()), lineInfo);
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
     * 列出Git存储库中的所有分支
     *
     * @throws IOException
     * @throws GitAPIException
     */
    public void listBranche() throws IOException, GitAPIException {

        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {

                List<Ref> branchList = git.branchList().call();

                for (Ref branch : branchList) {
                    System.out.println("Branch: " + branch + " " + branch.getName() + " " + branch.getObjectId().getName());
                }

                System.out.println("Now including remote branches:");

                branchList = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
                for (Ref branch : branchList) {
                    System.out.println("Branch: " + branch + " " + branch.getName() + " " + branch.getObjectId().getName());
                }
            }
        }
    }

    /**
     * 列出Git存储库中的所有Tag
     *
     * @throws IOException
     * @throws GitAPIException
     */
    public void ListTag() throws IOException, GitAPIException {
        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {

                List<Ref> tagList = git.tagList().call();

                for (Ref tag : tagList) {
                    System.out.println("Tag: " + tag + " " + tag.getName() + " " + tag.getObjectId().getName());

                    // fetch all commits for this tag
                    LogCommand log = git.log();

                    Ref peeledRef = repository.getRefDatabase().peel(tag);

                    if(peeledRef.getPeeledObjectId() != null) {
                        log.add(peeledRef.getPeeledObjectId());
                    } else {
                        log.add(tag.getObjectId());
                    }

                    Iterable<RevCommit> logs = log.call();
                    for (RevCommit rev : logs) {
                        System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    }
                }
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
        FileUtils.deleteDirectory(new File(localRepository));
    }

    /**
     * 返回本地存储库
     */
    public String getLocalRepository() {
        return localRepository;
    }
}
