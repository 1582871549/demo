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

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/9/20
 * @since 1.0.0
 */
@Data
public class JGitBean {

    private String url;
    private String username;
    private String password;
    private String localBranch;
    private String remoteBranch;

    private String serverAddress;
    private Integer serverPort;

    private String projectPath;
    private String dumpPath;

    public JGitBean(String username, String password, String localBranch, String projectPath, String dumpPath) {
        this.username = username;
        this.password = password;
        this.localBranch = localBranch;
        this.projectPath = projectPath;
        this.dumpPath = dumpPath;
    }
}
