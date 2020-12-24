package com.dudu.service.coverage.impl;

import com.dudu.manager.resource.git.helper.JGitHelper;
import com.dudu.manager.resource.git.entity.JGitBO;
import com.dudu.service.coverage.entity.CoverageProperty;
import com.dudu.manager.coverage.repository.entity.ProjectDO;
import com.dudu.manager.resource.git.entity.DiffClassBO;
import com.dudu.manager.resource.asm.service.AdapterManager;
import com.dudu.manager.coverage.service.CoverageManager;
import com.dudu.manager.resource.git.service.GetDiffCodeBlockStrategy;
import com.dudu.service.coverage.CoverageService;
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
public class CoverageServiceImpl implements CoverageService {

    private final AdapterManager adapterManager;
    private final CoverageManager coverageManager;
    private final CoverageProperty coverageProperty;

    @Autowired
    public CoverageServiceImpl(AdapterManager adapterManager, CoverageManager coverageManager, CoverageProperty coverageProperty) {
        this.adapterManager = adapterManager;
        this.coverageManager = coverageManager;
        this.coverageProperty = coverageProperty;
    }

    /**
     * 1、根据差异代码块获取策略, 获取代码差异信息。
     * 2、通过代码差异信息来匹配本次提交项目的差异方法
     * 3、最后将差异方法入库汇总, 进行数据分析
     *
     * @param comparisonStrategy 差异代码块获取策略
     * @param projectDO 项目信息
     */
    @Override
    public void callCoverageTask(GetDiffCodeBlockStrategy comparisonStrategy, ProjectDO projectDO) {

        // CoverageBO coverageBO = coverageProperty.createCoverageBO(projectDO);

        JGitBO jGitBO = coverageProperty.createJGitBO(projectDO);

        JGitHelper.createRepository(jGitBO);

        // 获取差异代码块信息
        Map<String, List<DiffClassBO>> diffMap = comparisonStrategy.getDiffCodeBlock(jGitBO);

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
