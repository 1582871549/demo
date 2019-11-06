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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.*;
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
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储库api封装类
 *
 * @author dujianwei
 * @create 2019/7/16
 * @since 1.0.0
 */
@Slf4j
public final class JGit {

    private final String url;
    private final String username;
    private final String password;
    private final String localBranch;

    public JGit(String url, String username, String password, String localBranch) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.localBranch = localBranch;
    }

    public void cloneRepository(String projectPath) throws IOException, GitAPIException {

        File repository = new File(projectPath);

        // 如果该文件夹存在则进行删除、避免克隆存储库时文件夹重复
        if (repository.exists()) {
            FileUtils.deleteDirectory(repository);
        }

        JGitHelper.mkdirsDirectory(repository);

        log.info("Cloning from " + url + " to " + projectPath + ", branch : " + localBranch);

        try (Git ignored = Git.cloneRepository()
                .setURI(url)
                .setBranch(localBranch)
                .setDirectory(repository)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call()) {
        }
    }

    public static void checkout(String repositoryPath, String branch) throws IOException, GitAPIException {
        try (Repository repository = JGitHelper.openRepository(repositoryPath)) {
            try (Git git = new Git(repository)) {
                git.checkout()
                        .setName("refs/heads/" + branch)
                        .call();
            }
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
    public Map<String, List<Integer>> diffTag(String repositoryPath, String remoteTag1, String remoteTag2) throws IOException, GitAPIException {

        Map<String, List<Integer>> insertMap = new HashMap<>(16);

        try (Repository repository = JGitHelper.openRepository(repositoryPath)) {
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

    /**
     * 列出Git存储库中的本地分支
     *
     * @return 本地分支
     */
    public List<Ref> listLocalBranch(String repositoryPath) throws IOException, GitAPIException {
        try (Repository repository = JGitHelper.openRepository(repositoryPath)) {
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
    public List<Ref> listRemoteBranch(String repositoryPath) throws IOException, GitAPIException {
        try (Repository repository = JGitHelper.openRepository(repositoryPath)) {
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
    public List<Ref> ListTag(String repositoryPath) throws IOException, GitAPIException {
        try (Repository repository = JGitHelper.openRepository(repositoryPath)) {
            try (Git git = new Git(repository)) {
                return git.tagList().call();
            }
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


}
