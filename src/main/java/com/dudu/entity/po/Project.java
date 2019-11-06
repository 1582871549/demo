/**
 * FileName: Project
 * Author:   大橙子
 * Date:     2019/10/24 15:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.entity.po;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/10/24
 * @since 1.0.0
 */
@Data
public class Project {

    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 远程存储库url
     */
    private String gitUrl;
    /**
     * 基础分支
     */
    private String localBranch;
}
