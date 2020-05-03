package com.dudu.service.coverage.impl;

import com.dudu.common.git.JGitHelper;
import com.dudu.entity.base.JGitBO;
import com.dudu.entity.bo.DiffClassBO;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.HunkHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengli
 * @create 2020/5/1
 * @since 1.0.0
 */
public abstract class AbstractCompareDiffTemplate {


    /**
     * 定义算法骨架
     *
     * @param jGitBO jGitBO
     * @return 代码差异块业务对象
     */
    public final Map<String, List<DiffClassBO>> compareDiff(JGitBO jGitBO) {

        String projectPath = jGitBO.getProjectPath();
        String base = jGitBO.getBase();
        String compare = jGitBO.getCompare();

        try (Repository repository = JGitHelper.openRepository(projectPath)) {

            List<DiffEntry> diffEntrys = getCodeDiff(repository, base, compare);

            return getDiff(repository, diffEntrys);
        }
    }

    private Map<String, List<DiffClassBO>> getDiff(Repository repository, List<DiffEntry> diffEntryList) {

        try {
            return getDiffClassBO(repository, diffEntryList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, List<DiffClassBO>> getDiffClassBO(Repository repository, List<DiffEntry> diffEntryList) throws IOException {

        Map<String, List<DiffClassBO>> diffClassBOMap = new HashMap<>(16);

        try (DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream())) {

            formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            formatter.setRepository(repository);

            for (DiffEntry diff : diffEntryList) {

                formatter.format(diff);
                DiffEntry.ChangeType changeType = diff.getChangeType();

                if (changeType == DiffEntry.ChangeType.ADD || changeType == DiffEntry.ChangeType.MODIFY) {

                    String classPath = diff.getNewPath();

                    List<DiffClassBO> diffClassBOS = new ArrayList<>(30);

                    for (HunkHeader hunk : formatter.toFileHeader(diff).getHunks()) {
                        for (Edit edit : hunk.toEditList()) {

                            Edit.Type type = edit.getType();

                            if (type == Edit.Type.INSERT || type == Edit.Type.REPLACE) {

                                int beginB = edit.getBeginB();
                                int endB = edit.getEndB();
                                // 实际差异开始行
                                int realBeginB = beginB + 1;

                                DiffClassBO diffClassBO = new DiffClassBO(realBeginB, endB);

                                diffClassBOS.add(diffClassBO);
                            }

                            if (type == Edit.Type.DELETE) {

                                int beginB = edit.getBeginB();
                                int endB = edit.getEndB();
                                // 实际差异开始行
                                int realBeginB = beginB + 1;

                                if (beginB == endB) {

                                    DiffClassBO diffClassBO = new DiffClassBO(realBeginB, realBeginB);
                                    diffClassBOS.add(diffClassBO);
                                } else {

                                    DiffClassBO diffClassBO = new DiffClassBO(realBeginB, endB);
                                    diffClassBOS.add(diffClassBO);
                                }
                            }
                        }
                    }
                    diffClassBOMap.put(classPath, diffClassBOS);
                }
            }
        }
        return diffClassBOMap;
    }

    /**
     * 获取差异代码快
     * @param repository 本次存储库对象
     * @param base 基础分支名称
     * @param compare 比对分支名称
     * @return 差异代码块集合
     */
    protected abstract List<DiffEntry> getCodeDiff(Repository repository, String base, String compare);

}
