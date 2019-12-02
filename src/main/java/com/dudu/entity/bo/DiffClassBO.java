package com.dudu.entity.bo;

import org.eclipse.jgit.diff.Edit;

/**
 * @author mengli
 * @create 2019/12/2
 * @since 1.0.0
 */
public class DiffClassBO {

    private int diffBegin;
    private int diffEnd;

    public DiffClassBO(int diffBegin, int diffEnd) {
        this.diffBegin = diffBegin;
        this.diffEnd = diffEnd;
    }

    @Override
    public String toString() {
        return "DiffClassBO{" +
                "diffBegin=" + diffBegin +
                ", diffEnd=" + diffEnd +
                '}';
    }

    public int getDiffBegin() {
        return diffBegin;
    }

    public void setDiffBegin(int diffBegin) {
        this.diffBegin = diffBegin;
    }

    public int getDiffEnd() {
        return diffEnd;
    }

    public void setDiffEnd(int diffEnd) {
        this.diffEnd = diffEnd;
    }
}
