package com.dudu.service.coverage;

import com.dudu.manager.coverage.repository.entity.ProjectDO;
import com.dudu.manager.resource.git.service.GetDiffCodeBlockStrategy;

/**
 * 覆盖率服务接口
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface CoverageService {

    /**
     * 调度一次覆盖率任务
     *
     * @param getDiffCodeBlockStrategy 差异代码块的获取策略
     * @param projectDO 项目信息
     */
    void callCoverageTask(GetDiffCodeBlockStrategy getDiffCodeBlockStrategy, ProjectDO projectDO);
}
