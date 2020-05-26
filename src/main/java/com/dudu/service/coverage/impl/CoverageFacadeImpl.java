package com.dudu.service.coverage.impl;

import com.dudu.manager.coverage.git.JGitHelper;
import com.dudu.manager.coverage.git.bo.JGitBO;
import com.dudu.manager.system.repository.entity.ProjectDO;
import com.dudu.manager.coverage.git.bo.DiffClassBO;
import com.dudu.manager.coverage.AdapterManager;
import com.dudu.manager.coverage.CoverageManager;
import com.dudu.service.coverage.CodeComparisonStrategy;
import com.dudu.service.coverage.CoverageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2020/3/18
 * @since 1.0.0
 */
@Service
public class CoverageFacadeImpl implements CoverageFacade {

    private final AdapterManager adapterManager;
    private final CoverageManager coverageManager;
    private final CoverageProperty coverageProperty;

    @Autowired
    public CoverageFacadeImpl(AdapterManager adapterManager, CoverageManager coverageManager, CoverageProperty coverageProperty) {
        this.adapterManager = adapterManager;
        this.coverageManager = coverageManager;
        this.coverageProperty = coverageProperty;
    }

    /**
     * 根据项目执行不同的覆盖率获取方法
     *
     * @param comparisonStrategy 覆盖率算法
     * @param projectDO git资源
     */
    @Override
    public void callCoverageService(CodeComparisonStrategy comparisonStrategy, ProjectDO projectDO) {

        // CoverageBO coverageBO = coverageProperty.createCoverageBO(projectDO);

        JGitBO jGitBO = coverageProperty.createJGitBO(projectDO);

        JGitHelper.createRepository(jGitBO);

        Map<String, List<DiffClassBO>> diffMap = comparisonStrategy.comparisonCode(jGitBO);

        Map<String, Map<String, String>> matchMethod = adapterManager.matchMethod(diffMap, jGitBO.getProjectPath());

        show(matchMethod);

        coverageManager.calculationChangeCoverage();

        // 保存覆盖率数据
    }

    private void show(Map<String, Map<String, String>> matchMethod) {

        System.out.println("=========================");

        for (Map.Entry<String, Map<String, String>> classEntry : matchMethod.entrySet()) {
            for (Map.Entry<String, String> methodEntry : classEntry.getValue().entrySet()) {
                System.out.println(classEntry.getKey() + " : " + methodEntry.getKey());
            }
        }

        System.out.println("=========================");
    }

}
