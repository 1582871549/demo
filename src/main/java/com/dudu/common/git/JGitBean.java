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

    private String className;
    private List<Integer> line;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Integer> getLine() {
        return line;
    }

    public void setLine(List<Integer> line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "JGitBean{" +
                "className='" + className + '\'' +
                ", line=" + line +
                '}';
    }
}
