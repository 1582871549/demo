package com.dudu.manager.git.service;

import com.dudu.manager.git.entity.JGitBO;
import com.dudu.manager.git.entity.DiffClassBO;
import com.dudu.manager.git.service.impl.BranchGetDiffCodeBlockStrategy;
import com.dudu.manager.git.service.impl.TagGetDiffCodeBlockStrategy;

import java.util.List;
import java.util.Map;

/**
 * 差异代码块获取策略接口
 *
 * @author 大橙子
 * @create 2020/3/18
 * @since 1.0.0
 * @pattern 策略模式
 */
public interface GetDiffCodeBlockStrategy {

    /**
     * 获取差异代码块
     *
     * @param jGitBO 将要执行覆盖率的项目信息
     * @return 差异代码块信息
     */
    Map<String, List<DiffClassBO>> getDiffCodeBlock(JGitBO jGitBO);


    /**
     * 创建不同的获取策略
     *
     * @pattern 简单工厂模式
     *
     * @param isBranch 是否为分支发布的项目
     * @return 差异代码块获取策略
     */
    static GetDiffCodeBlockStrategy createStrategy(boolean isBranch) {
        if (isBranch) {
            return new BranchGetDiffCodeBlockStrategy();
        } else {
            return new TagGetDiffCodeBlockStrategy();
        }
    }

}
