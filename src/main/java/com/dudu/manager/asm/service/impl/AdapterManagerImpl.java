package com.dudu.manager.asm.service.impl;

import com.dudu.manager.asm.service.AdapterManager;
import com.dudu.manager.git.entity.DiffClassBO;
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
    public Map<String, Map<String, String>> matchMethod(Map<String, List<DiffClassBO>> diffClassBOMap, String repositoryPath) {
        try {
            return JavaParserHelper.matchMethod(diffClassBOMap, repositoryPath);
        } catch (FileNotFoundException e) {
            // throw new BusinessException("match method failed", e);
            throw new RuntimeException("match method failed", e);
        }
    }

}
