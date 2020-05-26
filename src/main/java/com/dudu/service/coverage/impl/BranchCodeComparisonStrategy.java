package com.dudu.service.coverage.impl;

import com.dudu.manager.coverage.git.bo.JGitBO;
import com.dudu.manager.coverage.git.bo.DiffClassBO;
import com.dudu.service.coverage.CodeComparisonStrategy;

import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2020/3/18
 * @since 1.0.0
 */
public class BranchCodeComparisonStrategy implements CodeComparisonStrategy {

    @Override
    public Map<String, List<DiffClassBO>> comparisonCode(JGitBO jGitBO) {
        return new BranchCompareDiffTemplate().compareDiff(jGitBO);
    }

}
