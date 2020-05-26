package com.dudu.manager.system.repository.entity;

/**
 * 项目操作业务类
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public class ProjectDO {

    private Integer projectId;
    private String projectName;

    private String url;
    private String base;
    private String compare;

    private String serverAddress;
    private Integer serverPort;

    private boolean branch;

    public ProjectDO() {
    }

    public ProjectDO(Integer projectId, String projectName, String url, String base, String compare, String serverAddress, Integer serverPort, boolean branch) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = url;
        this.base = base;
        this.compare = compare;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.branch = branch;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
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

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
    }
}
