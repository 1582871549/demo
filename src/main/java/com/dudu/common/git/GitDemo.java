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
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
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

    private final String gitPath;
    private final String branchName;
    private final String username;
    private final String password;
    private final String repositoryDirName;
    private String localRepository = "C:\\Users\\大橙子\\AppData\\Local\\Temp\\gitRepository6919285836525817515";

    public GitDemo(String gitPath, String branchName, String username, String password, String repositoryDirName) {
        this.gitPath = gitPath;
        this.branchName = branchName;
        this.username = username;
        this.password = password;
        this.repositoryDirName = repositoryDirName;
    }

    public static void main(String[] args) {
        String gitPath = "https://github.com/1582871549/jacoco.git";
        String branchName = "master";
        String branchName2 = "dev";
        String username = "1582871549@qq.com";
        String password = "840742807du";
        String repositoryDirName = "gitRepository";

        GitDemo demo = new GitDemo(gitPath, branchName, username, password, repositoryDirName);

        // demo.cloneRepository();

        System.out.println();
        System.out.println();
        System.out.println("--------------------");
        System.out.println();

        // demo.ListTag();

        // demo.listBranche();

        demo.ShowBranchDiff(branchName, branchName2);

        // demo.blame("org.jacoco.core\\src\\org\\jacoco\\core\\analysis\\Analyzer.java");

    }

    /**
     * 克隆git存储库
     *
     * @throws IOException
     * @throws GitAPIException
     */
    private void cloneRepository() throws IOException, GitAPIException {

        // 为克隆的存储库准备一个临时文件夹
        File tempFolder = File.createTempFile(repositoryDirName, "");

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if(!tempFolder.delete()) {
            throw new IOException("Could not delete temporary folder " + tempFolder);
        }

        // then clone
        System.out.println("Cloning from " + gitPath + " to " + tempFolder + ", branch : " +branchName);
        try (Git result = Git.cloneRepository()
                .setURI(gitPath)
                .setBranch(branchName)
                .setDirectory(tempFolder)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                // .setProgressMonitor(new SimpleProgressMonitor())
                .call()) {
            // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
            System.out.println("Having repository: " + result.getRepository().getDirectory());

            this.localRepository = tempFolder.getPath();

            List<Ref> branchList = result.branchList().call();

            for (Ref branch : branchList) {
                System.out.println("Branch: " + branch + " " + branch.getName() + " " + branch.getObjectId().getName());
            }

            System.out.println("Now including remote branches:");

            branchList = result.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref branch : branchList) {
                System.out.println("Branch: " + branch + " " + branch.getName() + " " + branch.getObjectId().getName());
            }


        }

        // clean up here to not keep using more and more disk-space for these samples
        // FileUtils.deleteDirectory(tempFolder);
    }

    /**
     * 列出Git存储库中的所有分支
     *
     * @throws IOException
     * @throws GitAPIException
     */
    public void listBranche() throws IOException, GitAPIException {

        try (Repository repository = RepositoryHelper.openRepository(this.localRepository)) {
            System.out.println("Listing local branches:");
            try (Git git = new Git(repository)) {
                List<Ref> branchs = git.branchList().call();
                for (Ref branch : branchs) {
                    System.out.println("Branch: " + branch + " " + branch.getName() + " " + branch.getObjectId().getName());
                }

                System.out.println("Now including remote branches:");
                branchs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
                for (Ref branch : branchs) {
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
        try (Repository repository = RepositoryHelper.openRepository(localRepository)) {
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

    public void blame(String filePath) {
        try (Repository repository = RepositoryHelper.openRepository(localRepository)) {
            try (Git git = new Git(repository)) {
                BlameResult result = git.blame().setFilePath(filePath).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();

                RawText rawText = result.getResultContents();

                for (int i = 0; i < rawText.size(); i++) {
                    final PersonIdent sourceAuthor = result.getSourceAuthor(i);
                    final RevCommit sourceCommit = result.getSourceCommit(i);

                    System.out.println(sourceAuthor.getName() + " === ");

                    System.out.println(sourceAuthor.getName() + (sourceCommit != null ? "/" + sourceCommit.getCommitTime() + "/" + sourceCommit.getName() : "") + ": " + rawText.getString(i));
                }


            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示分支之间的差异
     *
     * @param remoteBranch 远程分支
     * @param localBranch 本地分支
     *
     * @throws IOException
     * @throws GitAPIException
     */
    public void ShowBranchDiff(String localBranch, String remoteBranch) {

        try (Repository repository = RepositoryHelper.openRepository(localRepository)) {
            try (Git git = new Git(repository)) {

                String localBranchName = "refs/heads/" + localBranch;
                String remoteBranchName = "refs/heads/" + remoteBranch;

                if(repository.exactRef(remoteBranchName) == null) {

                    // 首先，我们需要确保远程分支在本地是可见的。
                    Ref ref = git.branchCreate().setName(remoteBranch).setStartPoint("origin/" + remoteBranch).call();

                    System.out.println("Created local branch " + remoteBranch + ", with ref: " + ref);
                }

                // DIFF在TreeIterator上工作，我们为这两个分支准备了两个
                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, localBranchName);
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, remoteBranchName);

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

                        // System.out.println(out.toString("UTF-8"));

                        String fileName = StringUtils.replace(diff.getNewPath(), "/", ".");

                        System.out.println(fileName);

                        File diffFile = new File("D:\\aaa" + File.separator + fileName);

                        createDiffFile(diffFile);

                        try (FileOutputStream fos = new FileOutputStream(diffFile)) {
                            try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                                bos.write(out.toByteArray());
                            }
                        }

                        // System.out.println("Diff: " + diff.getChangeType() + ": " + (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
                        out.reset();
                        System.out.println("=================================start==============================");

                        //获取文件差异位置，从而统计差异的行数，如增加行数，减少行数
                        FileHeader fileHeader = formatter.toFileHeader(diff);
                        List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();
                        for(HunkHeader hunkHeader:hunks){
                            EditList editList = hunkHeader.toEditList();
                            for(Edit edit : editList){

                                System.out.println(edit.getType() + "   " + edit.getLengthA());

                                System.out.println("beginA:   " + edit.getBeginA() + "   endA:   " + edit.getEndA());
                                System.out.println("beginB:   " + edit.getBeginB() + "   endB:   " + edit.getEndB());

                            }
                        }

                        System.out.println("------------------------------end-----------------------------");
                    }
                }
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据指定路径生成新文件
     *
     * @param diffFile 差异文件路径
     * @throws IOException
     */
    private void createDiffFile(File diffFile) throws IOException {
        if (!diffFile.createNewFile()) {
            // 如果文件存在则进行删除
            if(!diffFile.delete()) {
                throw new IOException("Could not delete new diff file " + diffFile);
            } else {
                createDiffFile(diffFile);
            }
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String branchName) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        // 通过提交，我们可以构建树，它允许我们构建TreeParser
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

    // 统计指定版本代码总行数
    public long getAllFileLines(RevCommit commit) throws IOException {

        try (Repository repository = RepositoryHelper.openRepository(localRepository)) {
            try (TreeWalk treeWalk = new TreeWalk(repository)) {
                long size = 0;
                treeWalk.addTree(commit.getTree());
                treeWalk.setRecursive(true);
                MutableObjectId id = new MutableObjectId();
                while(treeWalk.next()){
                    treeWalk.getObjectId(id, 0);
                    String filePath = treeWalk.getPathString();
                    // if(filePathFilter.accept(new ChangeFile(filePath))){
                    //     int lines =countAddLine(BlobUtils.getContent(repository, id.toObjectId()));
                    //     size +=lines;
                    // }
                }
                return size;
            }
        }
    }

    /** 统计非空白行数
     * @param content
     * @return
     */
    public int countAddLine(String content){
        char[] chars = content.toCharArray();
        int sum = 0;
        boolean notSpace = false;
        for(char ch: chars){
            if(ch =='\n' && notSpace){
                sum++;
                notSpace = false;
            }else if(ch > ' '){
                notSpace = true;
            }
        }
        //最后一行没有换行时，如果有非空白字符，则+1
        if(notSpace){
            sum++;
        }
        return sum;
    }

    /**
     * 获取差异代码并切割到方法粒度
     *
     * 主要流程
     *      获取基线提交与被测提交之间的差异代码，然后过滤一些需要排除的文件（比如非 Java 文件、测试文件等等），
     *      对剩余文件进行解析，将变更代码解析到方法纬度
     *
     * @param diffs 差异集合
     */
    private void findDiffClasses(List<DiffEntry> diffs) {

        // 遍历差异列表集合
        for (DiffEntry diff : diffs) {

            DiffEntry.ChangeType changeType = diff.getChangeType();

            if (changeType == DiffEntry.ChangeType.ADD || changeType == DiffEntry.ChangeType.MODIFY){

                //     // 比对俩个版本 得出变化的方法set
                //     HashSet<String> changedMethods = MethodDiff.methodDiffInClass(oldPath, newPath);
                //     analyzeRequest.setMethodnames(changedMethods);
                // }
                //
                // classPath = gitDir + File.separator + diffEntry.getNewPath()
                //         .replace("src/main/java", "target/classes")
                //         .replace(".java", ".class");
                //
                // analyzeRequest.setClassesPath(classPath);
                // diffClasses.add(analyzeRequest);
            }
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
