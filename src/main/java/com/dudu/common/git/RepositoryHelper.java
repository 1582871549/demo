/**
 * FileName: CookbookHelper
 * Author:   大橙子
 * Date:     2019/7/9 15:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class RepositoryHelper {

    /**
     * 打开指定位置中的git存储库
     * <p>
     *     如果目录不存在,不会抛出异常. 也不会输出任何存储库信息
     * </p>
     *
     * @param localRepository 存储库路径
     *
     * @return 存储库示例
     * @throws IOException
     */
    public static Repository openRepository(String localRepository) throws IOException {
        return new FileRepositoryBuilder()
                .setGitDir(new File(localRepository, ".git"))
                .build();
    }

}
