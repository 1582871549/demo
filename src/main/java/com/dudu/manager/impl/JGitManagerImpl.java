package com.dudu.manager.impl;

import com.dudu.common.exception.BusinessException;
import com.dudu.common.git.JGitHelper;
import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.manager.JGitManager;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2019/11/25
 * @since 1.0.0
 */
@Service
public class JGitManagerImpl implements JGitManager {

    @Override
    public void cloneRepository(JGitBO jGitBO) {
        try {
            JGitHelper.cloneRepository(jGitBO);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, List<Integer>> comparisonBranch(JGitBO jGitBO) {
        try {
            return JGitHelper.compareBranchDiff(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("branch comparison failed", e);
        }
    }

    @Override
    public Map<String, List<Integer>> comparisonTag(JGitBO jGitBO) {
        try {
            return JGitHelper.compareTagDiff(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("tag comparison failed", e);
        }
    }

    @Override
    public void checkoutLocalBranch(JGitBO jGitBO) {
        try {
            JGitHelper.checkoutLocalBranch(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("checkout Local branch failed", e);
        }
    }

    public Map<String, List<DiffClassBO>> compareDiffTest(JGitBO jGitBO) {
        try {
            return JGitHelper.compareDiffTest(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("branch comparison failed", e);
        }
    }


}
