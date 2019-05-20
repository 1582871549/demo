/**
 * FileName: jacocoReport
 * Author:   大橙子
 * Date:     2019/4/18 16:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import com.dudu.common.exception.BusinessException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @date 2019/4/18
 * @since 1.0.0
 */
@Data
public class JacocoReport {

    /**
     * 关联用户
     */
    private String userId;
    /**
     * exec文件路径  与 this.executionDataFile 内容一致 repo存放, executionDataFile拿取
     */
    private String repo;
    /**
     * 服务器ip
     */
    private String address;
    /**
     * 服务器覆盖率端口
     */
    private String port;
    /**
     * project 项目目录
     */
    private String projectDirectory;
    /**
     * exec 报告导出目录
     */
    private String executionDataFile;
    /**
     * report 报告导出目录
     */
    private String reportDirectory;
    /**
     * diffReport 代码差异报告导出目录
     */
    private String diffReportDirectory;
    /**
     * java 文件目录
     */
    private String sourceDirectory;
    /**
     * class 文件目录
     */
    private String classDirectory;
    /**
     * html 报告标题
     */
    private String title;
    /**
     * diff 代码比对标识
     */
    private boolean diff;

    /**
     * 校验用户参数
     * repo exec文件路径
     * addr 服务器ip
     * port 服务器覆盖率端口
     */
    public void validate() {
        File file = new File(repo);
        if (!file.exists()) {
            throw new BusinessException("[coverage] exec file does not exist by userId = [ "+ userId +" ]");
        }
        if (StringUtils.isBlank(address)) {
            throw new BusinessException("[coverage] address can‘t be empty by userId = [ "+ userId +" ]");
        }
        if (StringUtils.isBlank(port)) {
            throw new BusinessException("[coverage] port can‘t be empty by userId = [ "+ userId +" ]");
        }
    }
}
