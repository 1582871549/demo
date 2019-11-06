/**
 * FileName: JGitBean
 * Author:   大橙子
 * Date:     2019/9/20 15:24
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/9/20
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JGitBean {

    private String path;
    private String gitUrl;
    private String username;
    private String password;
    private String localBranch;
    private String remoteBranch;
    private String repositoryPath;
}
