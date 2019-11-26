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

import com.dudu.entity.base.JGitBO;
import lombok.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/9/20
 * @since 1.0.0
 */
@Getter
@ToString(callSuper = true)
public class CoverageBO extends JGitBO {

    private static final long serialVersionUID = -5961902587615842543L;
    private final String serverAddress;
    private final Integer serverPort;
    private final String dumpPath;

    private CoverageBO(String url,
                       String username,
                       String password,
                       String defaultBranch,
                       String base,
                       String compare,
                       String projectPath,
                       String serverAddress,
                       Integer serverPort,
                       String dumpPath) {
        super(url, username, password, defaultBranch, base, compare, projectPath);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.dumpPath = dumpPath;
    }

    public static CoverageBO.CoverageBOBuilder builder() {
        return new CoverageBO.CoverageBOBuilder();
    }

    public static class CoverageBOBuilder {

        private String url;
        private String username;
        private String password;
        private String defaultBranch;
        private String base;
        private String compare;
        private String projectPath;

        private String serverAddress;
        private Integer serverPort;
        private String dumpPath;

        public CoverageBOBuilder() {
        }

        public CoverageBO.CoverageBOBuilder url(String url) {
            this.url = url;
            return this;
        }

        public CoverageBO.CoverageBOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CoverageBO.CoverageBOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CoverageBO.CoverageBOBuilder defaultBranch(String defaultBranch) {
            this.defaultBranch = defaultBranch;
            return this;
        }

        public CoverageBO.CoverageBOBuilder base(String base) {
            this.base = base;
            return this;
        }

        public CoverageBO.CoverageBOBuilder compare(String compare) {
            this.compare = compare;
            return this;
        }

        public CoverageBO.CoverageBOBuilder projectPath(String projectPath) {
            this.projectPath = projectPath;
            return this;
        }

        public CoverageBO.CoverageBOBuilder serverAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public CoverageBO.CoverageBOBuilder serverPort(Integer serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        public CoverageBO.CoverageBOBuilder dumpPath(String dumpPath) {
            this.dumpPath = dumpPath;
            return this;
        }

        public CoverageBO build() {
            return new CoverageBO(
                    this.url,
                    this.username,
                    this.password,
                    this.defaultBranch,
                    this.base,
                    this.compare,
                    this.projectPath,
                    this.serverAddress,
                    this.serverPort,
                    this.dumpPath);
        }
    }

}
