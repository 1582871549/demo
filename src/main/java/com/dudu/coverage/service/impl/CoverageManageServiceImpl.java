/**
 * FileName: CoverageManageServiceImpl
 * Author:   大橙子
 * Date:     2019/10/24 14:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.coverage.service.impl;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.common.git.JGitBean;
import com.dudu.common.git.JGitHelper;
import com.dudu.coverage.service.*;
import com.dudu.entity.bo.ProjectBO;
import com.dudu.entity.po.CopyDemo;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
public class CoverageManageServiceImpl implements CoverageManageService {

    private final GitProperties gitProperties;
    private final ExecProperties execProperties;

    private final ResourceService resourceService;
    private final ComparatorService comparatorService;
    private final AdapterService adapterService;
    private final CoverageService coverageService;

    @Override
    public void callCoverageService(ProjectBO projectBO) {

        JGitBean gitBean = getJGitBean(projectBO, gitProperties.getDefaultBranch());

        resourceService.prepareCoverageResource(gitBean);

        Map<String, List<Integer>> comparisonBranch = comparatorService.comparisonBranch(gitBean);

        checkoutRemoteBranch(gitBean);

        Map<String, Map<String, String>> matchMethod = adapterService.matchMethodName(comparisonBranch, gitBean.getProjectPath());

        coverageService.calculationChangeCoverage();

        // 保存覆盖率数据

    }

    private void checkoutRemoteBranch(JGitBean gitBean) {

        String projectPath = gitBean.getProjectPath();
        String remoteBranch = gitBean.getRemoteBranch();

        try {
            JGitHelper.checkout(projectPath, remoteBranch);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private JGitBean getJGitBean(ProjectBO projectBO, String defaultBranch) {

        String localBranch = projectBO.getLocalBranch();
        String username = gitProperties.getUsername();
        String password = gitProperties.getPassword();
        String projectPath = getProjectPath(projectBO);
        String dumpPath = getDumpPath(projectPath);

        JGitBean gitBean = new JGitBean(username, password, localBranch, projectPath, dumpPath);

        if (StringUtils.isBlank(localBranch)) {
            gitBean.setLocalBranch(defaultBranch);
        }
        CopyDemo.copyPropertiesIgnoreNull(projectBO, gitBean);
        return gitBean;
    }

    private String getProjectPath(ProjectBO projectBO) {

        String repositoryPath = gitProperties.getRepositoryPath();
        String projectId = String.valueOf(projectBO.getProjectId());
        String projectName = projectBO.getProjectName();

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

    @Autowired
    public CoverageManageServiceImpl(GitProperties gitProperties,
                                     ExecProperties execProperties, ResourceService resourceService,
                                     ComparatorService comparatorService,
                                     AdapterService adapterService,
                                     CoverageService coverageService) {
        this.gitProperties = gitProperties;
        this.execProperties = execProperties;
        this.resourceService = resourceService;
        this.comparatorService = comparatorService;
        this.adapterService = adapterService;
        this.coverageService = coverageService;
    }
}
