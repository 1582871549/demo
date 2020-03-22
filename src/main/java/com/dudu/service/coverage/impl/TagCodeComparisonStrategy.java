package com.dudu.service.coverage.impl;

import com.dudu.common.exception.BusinessException;
import com.dudu.common.git.JGitHelper;
import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.service.coverage.CodeComparisonStrategy;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2020/3/18
 * @since 1.0.0
 */
public class TagCodeComparisonStrategy implements CodeComparisonStrategy {

    @Override
    public Map<String, List<DiffClassBO>> comparisonCode(JGitBO jGitBO) {

        Map<String, List<DiffClassBO>> listMap = comparisonTag(jGitBO);

        checkoutLocalBranch(jGitBO);

        return listMap;
    }


    public Map<String, List<DiffClassBO>> comparisonTag(JGitBO jGitBO) {
        try {
            return JGitHelper.compareTagDiff(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("tag comparison failed", e);
        }
    }

    public void checkoutLocalBranch(JGitBO jGitBO) {
        try {
            JGitHelper.checkoutLocalBranch(jGitBO);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("checkout Local branch failed", e);
        }
    }
}
