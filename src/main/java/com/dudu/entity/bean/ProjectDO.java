package com.dudu.entity.bean;

import lombok.Data;

/**
 * 项目操作业务类
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Data
public class ProjectDO {

    private Integer projectId;
    private String projectName;

    private String url;
    private String base;
    private String compare;

    private String serverAddress;
    private Integer serverPort;

    private boolean branch;
}
