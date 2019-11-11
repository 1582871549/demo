package com.dudu.coverage.service.impl;

import com.dudu.common.configuration.bean.MavenProperties;
import com.dudu.common.git.JGitBean;
import com.dudu.common.git.JGitHelper;
import com.dudu.coverage.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
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

    private final MavenProperties mavenProperties;

    @Autowired
    public ResourceServiceImpl(MavenProperties mavenProperties) {
        this.mavenProperties = mavenProperties;
    }

    @Override
    public void prepareCoverageResource(JGitBean gitBean) {

        cloneCode(gitBean);

        compileCode(gitBean.getProjectPath());

        pullExecFileFromServer(gitBean);
    }

    private void cloneCode(JGitBean gitBean) {
        try {
            JGitHelper.cloneRepository(gitBean);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void compileCode(String projectPath) {
        try {
            if (executeCommand(projectPath) == 0) {
                log.info("success");
            } else {
                log.info("error");
            }
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }

    private int executeCommand(String projectPath) throws MavenInvocationException {

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

    public void pullExecFileFromServer(JGitBean gitBean) {
        try {
            pullExecFile(gitBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pullExecFile(JGitBean gitBean) throws IOException {

        String dumpPath = gitBean.getDumpPath();
        String address = gitBean.getServerAddress();
        Integer port = gitBean.getServerPort();

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
}
