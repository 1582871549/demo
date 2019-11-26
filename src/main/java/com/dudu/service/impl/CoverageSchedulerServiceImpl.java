package com.dudu.service.impl;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.entity.bo.CoverageBO;
import com.dudu.entity.bean.ProjectDO;
import com.dudu.manager.*;
import com.dudu.service.CoverageSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
    private JGitManager jGitManager;
    private AdapterManager adapterManager;
    private CoverageManager coverageManager;

    @Autowired
    public CoverageSchedulerServiceImpl(GitProperties gitProperties,
                                        ExecProperties execProperties,
                                        ResourceManager resourceManager,
                                        JGitManager jGitManager,
                                        AdapterManager adapterManager,
                                        CoverageManager coverageManager) {
        this.gitProperties = gitProperties;
        this.execProperties = execProperties;
        this.resourceManager = resourceManager;
        this.jGitManager = jGitManager;
        this.adapterManager = adapterManager;
        this.coverageManager = coverageManager;
    }

    @Override
    public void callCoverageService(ProjectDO projectDO) {

        CoverageBO coverageBO = createCoverageBO(projectDO);

        jGitManager.cloneRepository(coverageBO);

        Map<String, List<Integer>> branchDiffMap = jGitManager.comparisonBranch(coverageBO);

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

    @Override
    public void callCoverageServiceTag(ProjectDO projectDO) {

        CoverageBO coverageBO = createCoverageBO(projectDO);

        jGitManager.cloneRepository(coverageBO);

        Map<String, List<Integer>> tagDiffMap = jGitManager.comparisonTag(coverageBO);

        jGitManager.checkoutLocalBranch(coverageBO);

        Map<String, Map<String, String>> matchMethod = adapterManager.matchMethod(tagDiffMap, coverageBO.getProjectPath());

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

    private CoverageBO createCoverageBO(ProjectDO projectDO) {

        String url = projectDO.getUrl();
        String username = gitProperties.getUsername();
        String password = gitProperties.getPassword();
        String defaultBranch = gitProperties.getDefaultBranch();
        String projectPath = getProjectPath(projectDO);

        String base = projectDO.getBase();
        String compare = projectDO.getCompare();
        String serverAddress = projectDO.getServerAddress();
        Integer serverPort = projectDO.getServerPort();
        String dumpPath = getDumpPath(projectPath);

        CoverageBO.CoverageBOBuilder builder = CoverageBO.builder()
                .url(url)
                .username(username)
                .password(password)
                .defaultBranch(defaultBranch)
                .projectPath(projectPath)
                .base(base)
                .compare(compare)
                .serverAddress(serverAddress)
                .serverPort(serverPort)
                .dumpPath(dumpPath);

        if (projectDO.isBranch()) {
            builder.defaultBranch(compare);
        }
        return builder.build();
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
