package com.enlopo.gitlinkproj.api;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import git4idea.repo.GitBranchTrackInfo;
import git4idea.branch.GitBranchUtil;
import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;

import java.util.Collection;

import static org.eclipse.xtext.xbase.lib.InputOutput.print;

public class GitApi implements VcsApi {

    private final Project project;
    private final GitRepository repository;

    public GitApi(Project project, VirtualFile file) {
        this.project = project;
        this.repository = GitUtil.getRepositoryManager(project).getRepositoryForFileQuick(file);
    }

    public boolean isClean() {
        if (repository == null) {
            return false;
        }
        ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        Collection<Change> changes = changeListManager.getChangesIn(repository.getRoot());
        return changes.isEmpty();
    }

    public boolean hasUnpushedCommits() {
        if (repository == null || repository.getCurrentBranch() == null) {
            return false;
        }

        // Execute git command to count commits that are not pushed to any remote
        GitCommandResult result = Git.getInstance().runCommand(() ->
                new git4idea.commands.GitLineHandler(project, repository.getRoot(), git4idea.commands.GitCommand.REV_LIST)
                {{
                    addParameters("--count", "HEAD", "--not", "--remotes");
                }}
        );

        if (result.success()) {
            try {
                int commitCount = Integer.parseInt(result.getOutputOrThrow().trim());
                return commitCount > 0;
            } catch (NumberFormatException | VcsException e) {
                return false; // If parsing fails, assume no commits
            }
        }

        return false;
    }

    @Override
    public boolean isBranchOnRemote() {
        return repository != null && repository.getBranches().findBranchByName(repository.getCurrentBranch().getName()) != null;
    }

    @Override
    public String getBaseUrl() {
        if (repository == null || repository.getRemotes().isEmpty()) return "No remote URL found";
        return repository.getRemotes().iterator().next().getFirstUrl().replace(".git", "");
    }

    @Override
    public String getRepositoryRootPath() {
        return repository != null ? repository.getRoot().getPath() : "";
    }

    @Override
    public String getCurrentBranchName() {
        return repository != null && repository.getCurrentBranch() != null ? repository.getCurrentBranch().getName() : "No branch found";
    }

    @Override
    public VcsStatus checkVcsStatus() {
        if (!isClean()) return VcsStatus.UNCOMMITTED_CHANGES;
        if (hasUnpushedCommits()) return VcsStatus.UNPUSHED_COMMITS;
        if (!isBranchOnRemote()) return VcsStatus.BRANCH_NOT_ON_REMOTE;
        return VcsStatus.CLEAN;
    }
}

