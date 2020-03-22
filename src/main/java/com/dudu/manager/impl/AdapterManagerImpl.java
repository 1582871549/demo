package com.dudu.manager.impl;

import com.dudu.common.exception.BusinessException;
import com.dudu.common.git.JavaParserHelper;
import com.dudu.entity.bo.DiffClassBO;
import com.dudu.manager.AdapterManager;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Service
public class AdapterManagerImpl implements AdapterManager {

    @Override
    public Map<String, Map<String, String>> matchMethod(Map<String, List<Integer>> insertMap, String repositoryPath) {
        try {
            return JavaParserHelper.matchMethod(insertMap, repositoryPath);
        } catch (FileNotFoundException e) {
            throw new BusinessException("match method failed", e);
        }
    }

    @Override
    public Map<String, Map<String, String>> matchMethodTest(Map<String, List<DiffClassBO>> diffClassBOMap, String repositoryPath) {
        try {
            return JavaParserHelper.matchMethodTest(diffClassBOMap, repositoryPath);
        } catch (FileNotFoundException e) {
            throw new BusinessException("match method failed", e);
        }
    }

}
