package com.dudu.manager.git.entity;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/9/20
 * @since 1.0.0
 */
public class CoverageBO {

    private String serverAddress;
    private Integer serverPort;
    private String dumpPath;

    private JGitBO jGitBO;

    public CoverageBO(String serverAddress, Integer serverPort, String dumpPath, JGitBO jGitBO) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.dumpPath = dumpPath;
        this.jGitBO = jGitBO;
    }

    @Override
    public String toString() {
        return "CoverageBO{" +
                "serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                ", dumpPath='" + dumpPath + '\'' +
                ", jGitBO=" + jGitBO +
                '}';
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getDumpPath() {
        return dumpPath;
    }

    public void setDumpPath(String dumpPath) {
        this.dumpPath = dumpPath;
    }

    public JGitBO getjGitBO() {
        return jGitBO;
    }

    public void setjGitBO(JGitBO jGitBO) {
        this.jGitBO = jGitBO;
    }

}
