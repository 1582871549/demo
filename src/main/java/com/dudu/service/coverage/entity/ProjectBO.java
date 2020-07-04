package com.dudu.service.coverage.entity;

import lombok.Data;

/**
 * 项目操作业务类
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Data
public class ProjectBO {

    private Integer projectId;
    private String projectName;

    private String url;
    private String base;
    private String compare;

    private String serverAddress;
    private Integer serverPort;

    private boolean branch;

    public ProjectBO() {
    }

    public ProjectBO(Integer projectId, String projectName, String url, String base, String compare, String serverAddress, Integer serverPort, boolean branch) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = url;
        this.base = base;
        this.compare = compare;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.branch = branch;
    }
}
