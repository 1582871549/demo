package com.dudu.service.coverage;


import com.dudu.manager.system.repository.entity.ProjectDO;

/**
 * 覆盖率调度服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface CoverageFacade {

    /**
     *
     *
     * @param comparisonStrategy
     * @param projectDO
     */
    void callCoverageService(CodeComparisonStrategy comparisonStrategy, ProjectDO projectDO);
}
