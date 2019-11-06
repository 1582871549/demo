/**
 * FileName: ResourceServiceImpl
 * Author:   大橙子
 * Date:     2019/4/3 10:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.coverage.service.impl;

import com.dudu.common.configuration.bean.ExecProperties;
import com.dudu.common.configuration.bean.MavenProperties;
import com.dudu.common.configuration.bean.GitProperties;
import com.dudu.common.git.JGit;
import com.dudu.coverage.service.ResourceService;
import com.dudu.entity.bo.ProjectBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;

/**
 * 准备覆盖率任务执行时所需要的资源
 *
 * @author 大橙子
 * @create 2019/4/3
 * @since 1.0.0
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    private final GitProperties gitProperties;
    private final MavenProperties mavenProperties;
    private final ExecProperties execProperties;

    @Autowired
    public ResourceServiceImpl(GitProperties gitProperties, MavenProperties mavenProperties, ExecProperties execProperties) {
        this.gitProperties = gitProperties;
        this.mavenProperties = mavenProperties;
        this.execProperties = execProperties;
    }

    @Override
    public void prepareCoverageResource(ProjectBO projectBO) {

        cloneCode(projectBO);

        compileCode(projectBO);

        pullExecFileFromServer(projectBO);
    }

    public void cloneCode(ProjectBO projectBO) {
        try {
            cloneRepository(projectBO);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void cloneRepository(ProjectBO projectBO) throws IOException, GitAPIException {

        String projectPath = getProjectPath(projectBO);
        String defaultBranch = gitProperties.getDefaultBranch();

        JGit git = getJGit(projectBO, defaultBranch);

        git.cloneRepository(projectPath);
    }

    private String getProjectPath(ProjectBO projectBO) {

        String repositoryPath = gitProperties.getRepositoryPath();
        String projectId = String.valueOf(projectBO.getProjectId());
        String projectName = projectBO.getProjectName();

        return repositoryPath + File.separatorChar
                + projectId + File.separatorChar
                + projectName;
    }

    private JGit getJGit(ProjectBO projectBO, String defaultBranch) {

        String url = projectBO.getGitUrl();
        String username = gitProperties.getUsername();
        String password = gitProperties.getPassword();
        String branch = projectBO.getGitBranch();

        if (StringUtils.isBlank(branch)) {
            return new JGit(url, username, password, defaultBranch);
        }
        return new JGit(url, username, password, branch);
    }

    public void compileCode(ProjectBO projectBO) {
        try {
            if (executeCommand(projectBO) == 0) {
                log.info("success");
            } else {
                log.info("error");
            }
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }

    private int executeCommand(ProjectBO projectBO) throws MavenInvocationException {

        String projectPath = getProjectPath(projectBO);
        String command = mavenProperties.getCommand();
        String homePath = mavenProperties.getHomePath();

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(projectPath, "pom.xml"));
        request.setGoals(Collections.singletonList(command));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(homePath));

        invoker.setLogger(new PrintStreamLogger(System.err,  InvokerLogger.ERROR){});
        invoker.setOutputHandler(s -> { });

        InvocationResult execute = invoker.execute(request);
        return execute.getExitCode();
    }

    public void pullExecFileFromServer(ProjectBO projectBO) {
        try {
            pullExecFile(projectBO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pullExecFile(ProjectBO projectBO) throws IOException {

        String dumpPath = getDumpPath(projectBO);
        String address = projectBO.getServerAddress();
        Integer port = projectBO.getServerPort();

        try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
            if (!socket.isConnected()) {

            }
        }

        // 自动关流, 在外层处理异常
        // try (final FileOutputStream localFile = new FileOutputStream(dumpPath)) {
        //     try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
        //
        //         final ExecutionDataWriter localWriter = new ExecutionDataWriter(localFile);
        //
        //         RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
        //         RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());
        //
        //         reader.setSessionInfoVisitor(localWriter);
        //         reader.setExecutionDataVisitor(localWriter);
        //
        //         // 是否转储exec文件、是否重置探针信息
        //         writer.visitDumpCommand(true, false);
        //
        //         if (!reader.read()) {
        //             log.error("socket connection error!");
        //         }
        //     }
        // }
    }

    private String getDumpPath(ProjectBO projectBO) {

        String directory = execProperties.getDirectory();
        String defaultName = execProperties.getDefaultName();

        return getProjectPath(projectBO) + File.separatorChar
                + directory + File.separatorChar
                + defaultName;
    }

}
