package com.dudu.coverage.service;

import java.util.List;
import java.util.Map;

/**
 * 适配器服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface AdapterService {

    Map<String, Map<String, String>> matchMethodName(Map<String, List<Integer>> insertMap, String repositoryPath);

}
