package com.dudu.service.coverage;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

public interface CodeDiffGetStrategy {

    List<DiffEntry> getCodeDiff(Repository repository, String base, String compare);
}
