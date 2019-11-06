package com.dudu.coverage.service;

import com.dudu.entity.bo.ProjectBO;

/**
 * 资源调度服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface ResourceService {

    void prepareCoverageResource(ProjectBO projectBO);

}
