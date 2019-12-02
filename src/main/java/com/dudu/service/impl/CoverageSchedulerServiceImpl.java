package com.dudu.service.impl;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.entity.bo.CoverageBO;
import com.dudu.entity.bean.ProjectDO;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.entity.bo.MethodBO;
import com.dudu.manager.*;
import com.dudu.service.CoverageSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
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

        CoverageBO coverageBO = preMethod(projectDO);

        Map<String, List<Integer>> branchDiffMap = comparisonBranch(coverageBO);

        postMethod(branchDiffMap, coverageBO);
    }

    @Override
    public void callCoverageServiceTag(ProjectDO projectDO) {

        CoverageBO coverageBO = preMethod(projectDO);

        Map<String, List<Integer>> tagDiffMap = comparisonTag(coverageBO);

        postMethod(tagDiffMap, coverageBO);
    }

    private CoverageBO preMethod(ProjectDO projectDO) {

        CoverageBO coverageBO = createCoverageBO(projectDO);

        jGitManager.cloneRepository(coverageBO);

        return coverageBO;
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

    private Map<String,List<Integer>> comparisonBranch(CoverageBO coverageBO) {
        return jGitManager.comparisonBranch(coverageBO);
    }

    private Map<String,List<Integer>> comparisonTag(CoverageBO coverageBO) {

        Map<String, List<Integer>> tagDiffMap = jGitManager.comparisonTag(coverageBO);

        jGitManager.checkoutLocalBranch(coverageBO);

        return tagDiffMap;
    }

    private void postMethod(Map<String, List<Integer>> tagDiffMap, CoverageBO coverageBO) {

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


    @Override
    public void callCoverageServiceTest(ProjectDO projectDO) {

        CoverageBO coverageBO = preMethod(projectDO);

        Map<String, List<DiffClassBO>> diffClassBOMap = jGitManager.compareDiffTest(coverageBO);

        postMethodTest(diffClassBOMap, coverageBO);
    }

    private Map<String, Map<String, String>> postMethodTest(Map<String, List<DiffClassBO>> diffClassBOMap, CoverageBO coverageBO) {

        Map<String, MethodBO> methodBOMap = adapterManager.matchMethodTest(diffClassBOMap, coverageBO.getProjectPath());

        Map<String, Map<String, String>> diffClassMap = new HashMap<>(16);

        for (Map.Entry<String, List<DiffClassBO>> diffClassBOEntry : diffClassBOMap.entrySet()) {

            String classPath = diffClassBOEntry.getKey();

            System.out.println("11111111111111111111111");
            System.out.println("key  " + classPath);

            if (methodBOMap.get(classPath) == null) {
                continue;
            }

            MethodBO methodBO = methodBOMap.get(classPath);

            int methodBegin = methodBO.getMethodBegin();
            int methodEnd = methodBO.getMethodEnd();
            String methodName = methodBO.getMethodName();
            Map<String, String> diffMethodMap = new HashMap<>();

            for (DiffClassBO diffClassBO : diffClassBOEntry.getValue()) {

                int diffBegin = diffClassBO.getDiffBegin();
                int diffEnd = diffClassBO.getDiffEnd();

                /* 1、差异块小于等于方法块
                 * 2、差异块大于方法块
                 * 3、差异块只占用方法块的上半部分
                 * 4、差异块只占用方法块的下半部分
                 */
                if (diffBegin >= methodBegin && diffEnd <= methodEnd
                        || diffBegin < methodBegin && diffEnd > methodEnd
                        || diffBegin < methodBegin && diffEnd > methodBegin && diffEnd < methodEnd
                        || diffBegin > methodBegin && diffBegin < methodEnd && diffEnd > methodEnd) {

                    diffMethodMap.put(methodName, null);
                }
            }
            diffClassMap.put(classPath, diffMethodMap);
        }

        System.out.println("======================");
        System.out.println("======================");

        for (Map.Entry<String, Map<String, String>> stringMapEntry : diffClassMap.entrySet()) {

            System.out.println("------------------------");
            System.out.println("class : " + stringMapEntry.getKey());

            for (Map.Entry<String, String> entry : stringMapEntry.getValue().entrySet()) {

                String methodName = entry.getKey();
                System.out.println("method : " + methodName);
            }
            System.out.println("------------------------");
        }

        System.out.println("======================");
        System.out.println("======================");

        return diffClassMap;
    }

}
