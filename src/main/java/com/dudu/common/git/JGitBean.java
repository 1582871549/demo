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

import java.util.HashMap;
import java.util.Map;

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
    /**
     * old分支中增量方法的行号
     */
    private Map<String, String> line;

    public JGitBean(String className) {
        this.className = className;
        this.line = new HashMap<>(16);
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "JGitBean{" +
                "className='" + className + '\'' +
                ", line=" + line +
                '}';
    }
}
