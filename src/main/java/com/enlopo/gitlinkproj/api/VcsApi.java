package com.enlopo.gitlinkproj.api;

public interface VcsApi {
    boolean isClean();
    boolean hasUnpushedCommits();
    boolean isBranchOnRemote();
    String getBaseUrl();
    String getRepositoryRootPath();
    String getCurrentBranchName();
    VcsStatus checkVcsStatus();
}

