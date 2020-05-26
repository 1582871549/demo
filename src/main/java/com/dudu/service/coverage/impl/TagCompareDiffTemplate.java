package com.dudu.service.coverage.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2020/5/1
 * @since 1.0.0
 */
public class TagCompareDiffTemplate extends AbstractCompareDiffTemplate {


    @Override
    protected List<DiffEntry> getCodeDiff(Repository repository, String base, String compare) {

        try {
            return getDiffAndCreateTagPoint(repository, base, compare);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<DiffEntry> getDiffAndCreateTagPoint(Repository repository,
                                                     String baseTag,
                                                     String compareTag) throws GitAPIException, IOException {

        String baseTagName = "refs/tags/" + baseTag;
        String compareTagName = "refs/tags/" + compareTag;

        Map<String, ObjectId> tagMap = new HashMap<>();

        try (Git git = new Git(repository)) {

            for (Ref ref : git.tagList().call()) {

                System.out.println("--------------");
                System.out.println(ref.getName());
                System.out.println("--------------");

                if (baseTagName.equals(ref.getName())) {
                    tagMap.put("baseTag", ref.getObjectId());
                }
                if (compareTagName.equals(ref.getName())) {
                    tagMap.put("compareTag", ref.getObjectId());
                }
            }

            if (tagMap.size() != 2) {
                System.out.println("baseTag : " + baseTag + ",  compareTag : " + compareTag + ", tagMap : " +tagMap);
                // throw new BusinessException("tagMap.size() != 2");
                throw new RuntimeException();
            }

            try (RevWalk walk = new RevWalk(repository)) {

                RevCommit baseTagCommit = walk.parseCommit(tagMap.get("baseTag"));
                RevCommit compareTagCommit = walk.parseCommit(tagMap.get("compareTag"));

                git.branchCreate().setName(compareTag).setStartPoint(compareTagCommit).call();

                System.out.println(compareTag + "================================");

                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, baseTagCommit);
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, compareTagCommit);

                return git.diff()
                        .setOldTree(oldTreeParser)
                        .setNewTree(newTreeParser)
                        .setPathFilter(PathSuffixFilter.create(".java"))
                        .call();
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
