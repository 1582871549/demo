/**
 * FileName: AdapterServiceImpl
 * Author:   大橙子
 * Date:     2019/10/24 14:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.coverage.service.impl;

import com.dudu.common.git.JavaParserHelper;
import com.dudu.coverage.service.AdapterService;
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
public class AdapterServiceImpl implements AdapterService {

    @Override
    public Map<String, Map<String, String>> matchMethodName(Map<String, List<Integer>> insertMap, String repositoryPath) {

        try {

            JavaParserHelper.matchMethod(insertMap, repositoryPath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
