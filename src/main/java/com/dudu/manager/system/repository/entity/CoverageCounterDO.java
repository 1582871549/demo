package com.dudu.manager.system.repository.entity;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Data
public class CoverageCounterDO {

    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 覆盖率id
     */
    private Integer coverageId;
    /**
     * 行覆盖数
     */
    private Integer lineCovered;
    /**
     * 行未覆盖数
     */
    private Integer lineMissed;
    /**
     * 方法覆盖数
     */
    private Integer methodCovered;
    /**
     * 方法未覆盖数
     */
    private Integer methodMissed;
    /**
     * 类覆盖数
     */
    private Integer classCovered;
    /**
     * 类未覆盖数
     */
    private Integer classMissed;
    /**
     * 分支覆盖数
     */
    private Integer branchCovered;
    /**
     * 分支未覆盖数
     */
    private Integer branchMissed;
    /**
     * 圈覆盖数
     */
    private Integer complexityCovered;
    /**
     * 圈未覆盖数
     */
    private Integer complexityMissed;
    /**
     * 指令覆盖数
     */
    private Integer instructionCovered;
    /**
     * 指令未覆盖数
     */
    private Integer instructionMissed;

}

