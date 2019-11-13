/**
 * FileName: AdapterServiceImpl
 * Author:   大橙子
 * Date:     2019/10/24 14:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.manager.impl;

import com.dudu.common.exception.BusinessException;
import com.dudu.common.git.JavaParserHelper;
import com.dudu.manager.AdapterManager;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Service
public class AdapterManagerImpl implements AdapterManager {

    @Override
    public Map<String, Map<String, String>> matchMethod(Map<String, List<Integer>> insertMap, String repositoryPath) {
        return matchMethodName(insertMap, repositoryPath);
    }

    private Map<String, Map<String, String>> matchMethodName(Map<String, List<Integer>> insertMap, String repositoryPath) {
        try {
            return JavaParserHelper.matchMethod(insertMap, repositoryPath);
        } catch (FileNotFoundException e) {
            throw new BusinessException("match method failed", e);
        }
    }
}
