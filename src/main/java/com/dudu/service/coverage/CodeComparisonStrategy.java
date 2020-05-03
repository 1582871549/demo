package com.dudu.service.coverage;

import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.service.coverage.impl.BranchCodeComparisonStrategy;
import com.dudu.service.coverage.impl.TagCodeComparisonStrategy;

import java.util.List;
import java.util.Map;

/**
 * 覆盖率调度服务
 *
 * @author 大橙子
 * @create 2020/3/18
 * @since 1.0.0
 */
public interface CodeComparisonStrategy {

    /**
     *
     * @param jGitBO 将要执行覆盖率的项目信息
     * @return
     */
    Map<String, List<DiffClassBO>> comparisonCode(JGitBO jGitBO);


    /**
     * 创建不同的算法策略
     *
     * 简单工厂模式
     *
     * @param isBranch 是否测试分支覆盖率项目
     * @return 算法
     */
    static CodeComparisonStrategy getComparisonStrategy(boolean isBranch) {
        if (isBranch) {
            return new BranchCodeComparisonStrategy();
        } else {
            return new TagCodeComparisonStrategy();
        }
    }

}
