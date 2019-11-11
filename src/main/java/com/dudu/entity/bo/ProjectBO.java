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

    private Integer projectId;
    private String projectName;

    private String url;
    private String localBranch;
    private String remoteBranch;

    private String serverAddress;
    private Integer serverPort;
}
