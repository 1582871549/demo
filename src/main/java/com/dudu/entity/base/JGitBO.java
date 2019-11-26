package com.dudu.entity.base;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author mengli
 * @create 2019/11/25
 * @since 1.0.0
 */
@Getter
@ToString
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
}
