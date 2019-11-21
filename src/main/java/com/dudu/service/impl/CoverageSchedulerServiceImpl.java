package com.dudu.service.impl;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.entity.bo.CoverageBO;
import com.dudu.common.git.JGitHelper;
import com.dudu.entity.bean.ProjectDO;
import com.dudu.entity.bean.CopyDemo;
import com.dudu.manager.AdapterManager;
import com.dudu.manager.ComparatorManager;
import com.dudu.manager.CoverageManager;
import com.dudu.manager.ResourceManager;
import com.dudu.service.CoverageSchedulerService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Service
public class CoverageSchedulerServiceImpl implements CoverageSchedulerService {

    private GitProperties gitProperties;
    private ExecProperties execProperties;

    private ResourceManager resourceManager;
    private ComparatorManager comparatorManager;
    private AdapterManager adapterManager;
    private CoverageManager coverageManager;

    @Autowired
    public CoverageSchedulerServiceImpl(GitProperties gitProperties,
                                        ExecProperties execProperties,
                                        ResourceManager resourceManager,
                                        ComparatorManager comparatorManager,
                                        AdapterManager adapterManager,
                                        CoverageManager coverageManager) {
        this.gitProperties = gitProperties;
        this.execProperties = execProperties;
        this.resourceManager = resourceManager;
        this.comparatorManager = comparatorManager;
        this.adapterManager = adapterManager;
        this.coverageManager = coverageManager;
    }

    @Override
    public void callCoverageService(ProjectDO projectDO) {

        CoverageBO coverageBO = createCoverageBO(projectDO);

        resourceManager.prepareCoverageResource(coverageBO);

        Map<String, List<Integer>> branchDiffMap = comparatorManager.comparisonBranch(coverageBO);

        checkoutBranch(coverageBO);

        Map<String, Map<String, String>> matchMethod = adapterManager.matchMethod(branchDiffMap, coverageBO.getProjectPath());

        System.out.println("=========================");
        for (Map.Entry<String, Map<String, String>> classEntry : matchMethod.entrySet()) {
            for (Map.Entry<String, String> methodEntry : classEntry.getValue().entrySet()) {
                System.out.println(classEntry.getKey() + " : " + methodEntry.getKey());
            }
        }
        System.out.println("=========================");

        coverageManager.calculationChangeCoverage();

        // 保存覆盖率数据

    }

    private void checkoutBranch(CoverageBO coverageBO) {

        String projectPath = coverageBO.getProjectPath();
        String compareBranch = coverageBO.getCompareBranch();
        try {
            JGitHelper.checkoutLocalBranch(projectPath, compareBranch);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private CoverageBO createCoverageBO(ProjectDO projectDO) {

        String url = projectDO.getUrl();
        String username = gitProperties.getUsername();
        String password = gitProperties.getPassword();
        String baseBranch = projectDO.getBaseBranch();
        String compareBranch = projectDO.getCompareBranch();
        String serverAddress = projectDO.getServerAddress();
        Integer serverPort = projectDO.getServerPort();
        String projectPath = getProjectPath(projectDO);
        String dumpPath = getDumpPath(projectPath);

        return CoverageBO.builder()
                .url(url)
                .username(username)
                .password(password)
                .baseBranch(baseBranch)
                .compareBranch(compareBranch)
                .serverAddress(serverAddress)
                .serverPort(serverPort)
                .projectPath(projectPath)
                .dumpPath(dumpPath)
                .build();
    }

    private String getProjectPath(ProjectDO projectDO) {

        String repositoryPath = gitProperties.getRepositoryPath();
        String projectId = String.valueOf(projectDO.getProjectId());
        String projectName = projectDO.getProjectName();

        return repositoryPath + File.separatorChar
                + projectId + File.separatorChar
                + projectName;
    }

    private String getDumpPath(String projectPath) {

        String directory = execProperties.getDirectory();
        String defaultName = execProperties.getDefaultName();

        return projectPath + File.separatorChar
                + directory + File.separatorChar
                + defaultName;
    }

}
