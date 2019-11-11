/**
 * FileName: ComparatorServiceImpl
 * Author:   大橙子
 * Date:     2019/10/24 14:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.coverage.service.impl;

import com.dudu.common.git.JGitBean;
import com.dudu.common.git.JGitHelper;
import com.dudu.coverage.service.ComparatorService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
public class ComparatorServiceImpl implements ComparatorService {


    @Override
    public Map<String, List<Integer>> comparisonBranch(JGitBean gitBean) {


        try {
            return JGitHelper.getBranchDiff(gitBean);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, List<Integer>> comparisonTag(JGitBean bean) {

        return null;
    }
}
