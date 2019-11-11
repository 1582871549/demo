/**
 * FileName: ComparatorService
 * Author:   大橙子
 * Date:     2019/10/24 14:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.coverage.service;

import com.dudu.common.git.JGitBean;

import java.util.List;
import java.util.Map;

/**
 * 比较器服务
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
public interface ComparatorService {

    Map<String, List<Integer>> comparisonBranch(JGitBean bean);

    Map<String, List<Integer>> comparisonTag(JGitBean bean);
}
