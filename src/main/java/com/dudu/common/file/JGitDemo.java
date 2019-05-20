/**
 * FileName: JGitDemo
 * Author:   大橙子
 * Date:     2019/4/18 15:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 大橙子
 * @date  2019/4/18
 * @since 1.0.0
 */
public class JGitDemo {

    /**
     * 获取差异代码并切割到方法粒度
     *
     * 主要流程
     *      获取基线提交与被测提交之间的差异代码，然后过滤一些需要排除的文件（比如非 Java 文件、测试文件等等），
     *      对剩余文件进行解析，将变更代码解析到方法纬度
     *
     * @return
     * @throws GitAPIException
     * @throws IOException
     */
    private List<AnalyzeRequest> findDiffClasses() throws GitAPIException, IOException {

        String gitAppName = "appname";
        String repoURL = "www.github.com";

        String gitDir = "D://" + File.separator + gitAppName;

        String branchName = "dev";

        // 克隆分支  repoURL ,gitDir,branchName
        // clone();

        // 提交的版本id
        String masterCommit = "";

        // 差异列表 git仓库url, 本地保存目录, 当前提交版本id, 之前的提交版本id
        List<DiffEntry> diffList = new ArrayList<>();

        List< AnalyzeRequest> diffClasses = new ArrayList<>();

        String classPath;

        // 遍历差异列表集合
        for (DiffEntry diff : diffList) {

            if (diff.getChangeType() == DiffEntry.ChangeType.DELETE){
                continue;
            }
            AnalyzeRequest analyzeRequest = new AnalyzeRequest();

            if (diff.getChangeType() == DiffEntry.ChangeType.ADD){

            } else {
                // 比对俩个版本 得出变化的方法set
                // HashSet<String> changedMethods = MethodDiff.methodDiffInClass(oldPath, newPath);
                // analyzeRequest.setMethodnames(changedMethods);
            }

            classPath = gitDir + File.separator + diff.getNewPath()
                    .replace("src/main/java", "target/classes")
                    .replace(".java", ".class");

            analyzeRequest.setClassesPath(classPath);
            diffClasses.add(analyzeRequest);
        }

        return diffClasses;
    }

    public List<DiffEntry> diff(Repository repository, String startCommit, String endCommit) {

        AbstractTreeIterator startTreeIterator = prepareTreeParser(repository, startCommit);
        AbstractTreeIterator endTreeIterator = prepareTreeParser(repository, endCommit);

        Git git = new Git(repository);

        List<DiffEntry> diffList = new ArrayList<>();

        try {
            diffList = git.diff().setOldTree(startTreeIterator).setNewTree(endTreeIterator).setPathFilter(PathSuffixFilter.create(".java")).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return diffList;
    }

    public AbstractTreeIterator prepareTreeParser(Repository repository, String commitId) {

        RevWalk walk = new RevWalk(repository);
        CanonicalTreeParser treeParser = null;
        try {
            RevCommit commit = walk.parseCommit(ObjectId.fromString(commitId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            treeParser = new CanonicalTreeParser();
            ObjectReader reader = repository.newObjectReader();
            treeParser.reset(reader, tree.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        walk.dispose();
        return treeParser;
    }
}
