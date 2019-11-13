package com.dudu.manager;

import com.dudu.entity.bo.CoverageBO;

/**
 * 本地资源调度服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface ResourceManager {

    void prepareCoverageResource(CoverageBO coverageBO);

}
