package com.dudu.entity.bo;

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

    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 远程存储库url
     */
    private String gitUrl;
    /**
     * 基础分支
     */
    private String gitBranch;
    /**
     * 项目id
     */
    private String serverAddress;
    /**
     * 项目id
     */
    private Integer serverPort;
}
