package com.dudu.manager.coverage;

import com.dudu.manager.coverage.git.bo.DiffClassBO;

import java.util.List;
import java.util.Map;

/**
 * 适配器服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface AdapterManager {

    Map<String, Map<String, String>> matchMethod(Map<String, List<DiffClassBO>> diffClassBOMap, String repositoryPath);

}
