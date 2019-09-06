/**
 * FileName: JGitBean
 * Author:   大橙子
 * Date:     2019/7/15 20:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 存放比对数据
 *
 * @author 大橙子
 * @create 2019/7/15
 * @since 1.0.0
 */
public class JGitBean {

    /**
     * 差异类名称
     */
    private String className;
    /**
     * 比对分支差异结束行
     */
    private List<Integer> lineB;

    /**
     * 创建一个bean实例
     *
     * @param className 差异类名
     * @param lineB 差异行集合
     */
    public JGitBean(String className, List<Integer> lineB) {
        this.className = className;
        this.lineB = lineB;
    }

    public String getClassName() {
        return className;
    }

    public List<Integer> getLineB() {
        return lineB;
    }

    @Override
    public String toString() {
        return "JGitBean{" +
                "className='" + className + '\'' +
                ", lineB=" + lineB +
                '}';
    }
}
