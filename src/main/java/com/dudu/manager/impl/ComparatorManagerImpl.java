/**
 * FileName: ComparatorServiceImpl
 * Author:   大橙子
 * Date:     2019/10/24 14:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.manager.impl;

import com.dudu.common.exception.BusinessException;
import com.dudu.entity.bo.CoverageBO;
import com.dudu.common.git.JGitHelper;
import com.dudu.manager.ComparatorManager;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Service
public class ComparatorManagerImpl implements ComparatorManager {

    @Override
    public Map<String, List<Integer>> comparisonBranch(CoverageBO coverageBO) {

        String projectPath = coverageBO.getProjectPath();
        String baseBranch = coverageBO.getBaseBranch();
        String compareBranch = coverageBO.getCompareBranch();

        try {
            return JGitHelper.compareBranchDiff(projectPath, baseBranch, compareBranch);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("branch comparison failed", e);
        }
    }

    @Override
    public Map<String, List<Integer>> comparisonTag(CoverageBO coverageBO) {

        String projectPath = coverageBO.getProjectPath();
        String baseTag = coverageBO.getBaseBranch();
        String compareTag = coverageBO.getCompareBranch();

        try {
            return JGitHelper.compareTagDiff(projectPath, baseTag, compareTag);
        } catch (IOException | GitAPIException e) {
            throw new BusinessException("branch comparison failed", e);
        }
    }
}
