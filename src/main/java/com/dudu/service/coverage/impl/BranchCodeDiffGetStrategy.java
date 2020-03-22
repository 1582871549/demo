package com.dudu.service.coverage.impl;

import com.dudu.service.coverage.CodeDiffGetStrategy;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.IOException;
import java.util.List;

/**
 * @author mengli
 * @create 2020/3/22
 * @since 1.0.0
 */
public class BranchCodeDiffGetStrategy implements CodeDiffGetStrategy {

    @Override
    public List<DiffEntry> getCodeDiff(Repository repository, String base, String compare) {

        try {
            return getDiffAndCreateBranchPoint(repository, base, compare);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DiffEntry> getDiffAndCreateBranchPoint(Repository repository,
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
}
