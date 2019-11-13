package com.dudu.manager;

import com.dudu.entity.bo.CoverageBO;

import java.util.List;
import java.util.Map;

/**
 * 比较器服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface ComparatorManager {

    Map<String, List<Integer>> comparisonBranch(CoverageBO coverageBO);

    Map<String, List<Integer>> comparisonTag(CoverageBO coverageBO);
}
