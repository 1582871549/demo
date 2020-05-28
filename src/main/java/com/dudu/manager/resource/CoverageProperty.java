package com.dudu.manager.resource;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.manager.git.entity.JGitBO;
import com.dudu.manager.system.repository.entity.ProjectDO;
import com.dudu.manager.git.entity.CoverageBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author mengli
 * @create 2019/12/10
 * @since 1.0.0
 */
@Configuration
public class CoverageProperty {

    private GitProperties gitProperties;
    private ExecProperties execProperties;

    @Autowired
    public CoverageProperty(GitProperties gitProperties, ExecProperties execProperties) {
        this.gitProperties = gitProperties;
        this.execProperties = execProperties;
    }

    public CoverageBO createCoverageBO(ProjectDO projectDO) {

        JGitBO jGitBO = createJGitBO(projectDO);
        String serverAddress = projectDO.getServerAddress();
        Integer serverPort = projectDO.getServerPort();
        String dumpPath = getDumpPath(jGitBO.getProjectPath());

        return new CoverageBO(serverAddress, serverPort, dumpPath, jGitBO);
    }

    public JGitBO createJGitBO(ProjectDO projectDO) {

        String url = projectDO.getUrl();
        String username = gitProperties.getUsername();
        String password = gitProperties.getPassword();
        String defaultBranch = gitProperties.getDefaultBranch();
        String base = projectDO.getBase();
        String compare = projectDO.getCompare();
        String projectPath = getProjectPath(projectDO);

        if (projectDO.isBranch()) {
            defaultBranch = compare;
        }

        return new JGitBO(
                url,
                username,
                password,
                defaultBranch,
                base,
                compare,
                projectPath
        );
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
