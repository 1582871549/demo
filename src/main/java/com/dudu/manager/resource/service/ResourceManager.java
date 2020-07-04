package com.dudu.manager.resource.service;

/**
 * 本地资源调度服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface ResourceManager {

    void compileCode(String projectPath);

    void pullExecFileFromServer(String dumpPath, String address, int port);

}
