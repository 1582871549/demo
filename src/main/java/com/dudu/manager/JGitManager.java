package com.dudu.manager;

import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;

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

    Map<String, List<DiffClassBO>> comparisonBranch(JGitBO jGitBO);

    Map<String, List<DiffClassBO>> comparisonTag(JGitBO jGitBO);

    void checkoutLocalBranch(JGitBO jGitBO);


}
