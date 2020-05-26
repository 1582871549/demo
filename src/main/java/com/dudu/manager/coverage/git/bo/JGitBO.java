package com.dudu.manager.coverage.git.bo;

import java.io.Serializable;

/**
 * @author mengli
 * @create 2019/11/25
 * @since 1.0.0
 */
public class JGitBO implements Serializable {

    private static final long serialVersionUID = 5467154959546816889L;
    private final String url;
    private final String username;
    private final String password;
    private final String defaultBranch;
    private final String base;
    private final String compare;
    private final String projectPath;

    public JGitBO(String url,
                  String username,
                  String password,
                  String defaultBranch,
                  String base,
                  String compare,
                  String projectPath) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.defaultBranch = defaultBranch;
        this.base = base;
        this.compare = compare;
        this.projectPath = projectPath;
    }

    @Override
    public String toString() {
        return "JGitBO{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", base='" + base + '\'' +
                ", compare='" + compare + '\'' +
                ", projectPath='" + projectPath + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public String getBase() {
        return base;
    }

    public String getCompare() {
        return compare;
    }

    public String getProjectPath() {
        return projectPath;
    }
}
