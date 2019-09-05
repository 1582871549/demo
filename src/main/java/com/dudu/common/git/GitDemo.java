/**
 * FileName: GitDemo
 * Author:   大橙子
 * Date:     2019/4/10 16:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.*;
import java.util.List;

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

    private final String gitUrl;
    private final String localBranch;
    private final String username;
    private final String password;
    private final String repositoryDirName;
    private String localRepository = "C:\\Users\\大橙子\\AppData\\Local\\Temp\\gitRepository6977493259078172857";

    public GitDemo(String gitUrl, String localBranch, String username, String password, String repositoryDirName) {
        this.gitUrl = gitUrl;
        this.localBranch = localBranch;
        this.username = username;
        this.password = password;
        this.repositoryDirName = repositoryDirName;
    }

    public static void main(String[] args) throws IOException, GitAPIException {
        String gitPath = "https://github.com/1582871549/jacoco.git";
        String branchName = "master";
        String branchName2 = "dev";
        String username = "1582871549@qq.com";
        String password = "840742807du";
        String repositoryDirName = "gitRepository";

        GitDemo demo = new GitDemo(gitPath, branchName, username, password, repositoryDirName);

        // String s = demo.cloneRepository();

        System.out.println("=======================================");

        // demo.listBranche();

        System.out.println("=======================================");

        demo.ListTag();

        demo.diffTag("v.1.0", "v.1.1");

        // demo.diffBranch(branchName2);
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
     * 克隆git存储库
     *
     * @throws IOException
     * @throws GitAPIException
     */
    private String cloneRepository() throws IOException, GitAPIException {

        // 为克隆的存储库准备一个临时文件夹
        File tempFolder = File.createTempFile(repositoryDirName, "");

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if(!tempFolder.delete()) {
            throw new IOException("Could not delete temporary folder " + tempFolder);
        }

        // then clone
        System.out.println("Cloning from " + gitUrl + " to " + tempFolder + ", branch : " + localBranch);

        try (Git result = Git.cloneRepository()
                .setURI(gitUrl)
                .setBranch(localBranch)
                .setDirectory(tempFolder)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                // .setProgressMonitor(new SimpleProgressMonitor())
                .call()) {

            System.out.println("Having repository: " + result.getRepository().getDirectory());

            return this.localRepository = tempFolder.getPath();
        }
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

                branchList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
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
     * 显示分支之间的差异
     *
     * @param remoteBranch 远程分支
     *
     * @throws IOException
     * @throws GitAPIException
     */
    public void diffBranch(String remoteBranch) {

        try (Repository repository = openRepository()) {
            try (Git git = new Git(repository)) {

                String localBranchName = "refs/heads/" + localBranch;
                String remoteBranchName = "refs/heads/" + remoteBranch;

                if(repository.exactRef(remoteBranchName) == null) {

                    // 首先，我们需要确保远程分支在本地是可见的。
                    Ref ref = git.branchCreate().setName(remoteBranch).setStartPoint("origin/" + remoteBranch).call();

                    System.out.println("Created local branch " + remoteBranch + ", with ref: " + ref);
                }

                // DIFF在TreeIterator上工作，我们为这两个分支准备了两个
                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, repository.exactRef(localBranchName));
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, repository.exactRef(remoteBranchName));

                // then the procelain diff-command returns a list of diff entries
                // 然后procelain diff-命令返回一个diff条目列表。
                List<DiffEntry> diffs = git.diff()
                        .setOldTree(oldTreeParser)
                        .setNewTree(newTreeParser)
                        .setPathFilter(PathSuffixFilter.create(".java"))
                        .call();

                System.out.println("================================");

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                try (DiffFormatter formatter = new DiffFormatter(out)) {

                    formatter.setRepository(repository);

                    // 设置比较器 忽略全部空白字符
                    formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);

                    //每一个diffEntry都是第个文件版本之间的变动差异
                    for (DiffEntry diff : diffs) {

                        //打印文件差异具体内容
                        formatter.format(diff);

                        System.out.println(out.toString("UTF-8"));

                        System.out.println("Diff: " + diff.getChangeType() + ": " + (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
                        out.reset();

                        System.out.println("=================================start==============================");

                        //获取文件差异位置，从而统计差异的行数，如增加行数，减少行数

                            //获取文件差异位置
                            for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                                for (Edit edit : hunk.toEditList()) {
                                    System.out.println("beginA:   " + edit.getBeginA() + "   endA:   " + edit.getEndA());
                                    System.out.println("beginB:   " + edit.getBeginB() + "   endB:   " + edit.getEndB());
                                }
                            }

                        System.out.println("==================================end===============================");
                    }
                }
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void diffTag(String remoteTag1, String remoteTag2) throws IOException, GitAPIException {
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
                    return;
                }

                try (RevWalk walk = new RevWalk(repository)) {
                    RevCommit commit1 = walk.parseCommit(tag1Id);
                    RevCommit commit2 = walk.parseCommit(tag2Id);

                    // DIFF在TreeIterator上工作，我们为这两个分支准备了两个
                    AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, commit1);
                    AbstractTreeIterator newTreeParser = prepareTreeParser(repository, commit2);

                    // then the procelain diff-command returns a list of diff entries
                    // 然后procelain diff-命令返回一个diff条目列表。
                    List<DiffEntry> diffs = git.diff()
                            .setOldTree(oldTreeParser)
                            .setNewTree(newTreeParser)
                            .setPathFilter(PathSuffixFilter.create(".java"))
                            .call();

                    System.out.println("================================");

                    try (DiffFormatter formatter = new DiffFormatter(System.out)) {

                        // 设置比较器 忽略全部空白字符
                        formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
                        formatter.setRepository(repository);

                        // 每一个diffEntry都是第个文件版本之间的变动差异
                        for (DiffEntry diff : diffs) {

                            //打印文件差异具体内容
                            formatter.format(diff);

                            System.out.println("=================================start==============================");

                            //获取文件差异位置，从而统计差异的行数，如增加行数，减少行数

                            //获取文件差异位置
                            for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                                for (Edit edit : hunk.toEditList()) {
                                    System.out.println("beginA:   " + edit.getBeginA() + "   endA:   " + edit.getEndA());
                                    System.out.println("beginB:   " + edit.getBeginB() + "   endB:   " + edit.getEndB());
                                }
                            }

                            System.out.println("==================================end===============================");
                        }
                    }
                    walk.dispose();
                }
            }
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, Ref branchRef) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        // 通过提交，我们可以构建树，它允许我们构建TreeParser
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


    private void showStatus() throws GitAPIException, IOException {
        Git git = Git.open(new File("D:\\source-code\\temp-1\\.git"));
        Status status = git.status().call();        //返回的值都是相对工作区的路径，而不是绝对路径
        status.getAdded().forEach(it -> System.out.println("Add File :" + it));      //git add命令后会看到变化
        status.getRemoved().forEach(it -> System.out.println("Remove File :" + it));  ///git rm命令会看到变化，从暂存区删除的文件列表
        status.getModified().forEach(it -> System.out.println("Modified File :" + it));  //修改的文件列表
        status.getUntracked().forEach(it -> System.out.println("Untracked File :" + it)); //工作区新增的文件列表
        status.getConflicting().forEach(it -> System.out.println("Conflicting File :" + it)); //冲突的文件列表
        status.getMissing().forEach(it -> System.out.println("Missing File :" + it));    //工作区删除的文件列表
    }

//     //提取某个作者的提交，并打印相关信息
//     Git git = Git.open(new File("D:\\source-code\\temp-1\\.git"));
//     DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//     Iterable<RevCommit> results = git.log().setRevFilter(new RevFilter() {
//         @Override
//         public boolean include(RevWalk walker, RevCommit cmit)
//                 throws StopWalkException, MissingObjectException, IncorrectObjectTypeException, IOException {
//             return cmit.getAuthorIdent().getName().equals("xxxxx dsd");
//         }
//
//         @Override
//         public RevFilter clone() {
//             return this;
//         }
//     }).call();
// results.forEach(commit -> {
//         PersonIdent authoIdent = commit.getAuthorIdent();
//         System.out.println("提交人：  " + authoIdent.getName() + "     <" + authoIdent.getEmailAddress() + ">");
//         System.out.println("提交SHA1：  " + commit.getId().name());
//         System.out.println("提交信息：  " + commit.getShortMessage());
//         System.out.println("提交时间：  " + format.format(authoIdent.getWhen()));
//     });



    // DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Repository repository = new RepositoryBuilder().setGitDir(new File("D:\\source-code\\temp-1\\.git")).build();
    //     try (RevWalk walk = new RevWalk(repository)) {
    //     Ref head = repository.findRef("HEAD");
    //     walk.markStart(walk.parseCommit(head.getObjectId())); // 从HEAD开始遍历，
    //     for (RevCommit commit : walk) {
    //         RevTree tree = commit.getTree();
    //
    //         TreeWalk treeWalk = new TreeWalk(repository, repository.newObjectReader());
    //         PathFilter f = PathFilter.create("pom.xml");
    //         treeWalk.setFilter(f);
    //         treeWalk.reset(tree);
    //         treeWalk.setRecursive(false);
    //         while (treeWalk.next()) {
    //             PersonIdent authoIdent = commit.getAuthorIdent();
    //             System.out.println("提交人： " + authoIdent.getName() + " <" + authoIdent.getEmailAddress() + ">");
    //             System.out.println("提交SHA1： " + commit.getId().name());
    //             System.out.println("提交信息： " + commit.getShortMessage());
    //             System.out.println("提交时间： " + format.format(authoIdent.getWhen()));
    //
    //             ObjectId objectId = treeWalk.getObjectId(0);
    //             ObjectLoader loader = repository.open(objectId);
    //             loader.copyTo(System.out);              //提取blob对象的内容
    //         }
    //     }
    // }

    /**
     * 克隆进度显示器
     */
    private static class SimpleProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
            System.out.print(completed + "-");
        }

        @Override
        public void endTask() {
            System.out.println("Done");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }

}
