/**
 * FileName: JGitBean
 * Author:   大橙子
 * Date:     2019/9/20 15:24
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.entity.bo;

import lombok.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/9/20
 * @since 1.0.0
 */
@Builder
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CoverageBO {

    private final String url;
    private final String username;
    private final String password;
    private final String baseBranch;
    private final String compareBranch;

    private final String serverAddress;
    private final Integer serverPort;

    private final String projectPath;
    private final String dumpPath;
}
