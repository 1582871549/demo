package com.dudu.service.coverage.impl;

import com.dudu.common.git.JGitHelper;
import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.service.coverage.CodeComparisonStrategy;

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

        Map<String, List<DiffClassBO>> listMap = new TagCompareDiffTemplate().compareDiff(jGitBO);

        JGitHelper.checkoutBranch(jGitBO);

        return listMap;
    }
}
