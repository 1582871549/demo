package com.dudu.manager.git.service.impl;

import com.dudu.manager.git.entity.JGitBO;
import com.dudu.manager.git.entity.DiffClassBO;
import com.dudu.manager.git.service.GetDiffCodeBlockStrategy;

import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2020/3/18
 * @since 1.0.0
 */
public class BranchGetDiffCodeBlockStrategy implements GetDiffCodeBlockStrategy {

    @Override
    public Map<String, List<DiffClassBO>> getDiffCodeBlock(JGitBO jGitBO) {
        return new BranchGetDiffCodeBlockTemplate().compareDiff(jGitBO);
    }

}
