package com.dudu.manager;

import com.dudu.entity.base.JGitBO;

import java.util.List;
import java.util.Map;

/**
 * JGit api 封装
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface JGitManager {

    void cloneRepository(JGitBO jGitBO);

    Map<String, List<Integer>> comparisonBranch(JGitBO jGitBO);

    Map<String, List<Integer>> comparisonTag(JGitBO jGitBO);

    void checkoutLocalBranch(JGitBO jGitBO);
}
