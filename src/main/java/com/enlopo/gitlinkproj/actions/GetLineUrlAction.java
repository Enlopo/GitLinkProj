package com.enlopo.gitlinkproj.actions;

import com.enlopo.gitlinkproj.ui.UrlDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.enlopo.gitlinkproj.api.VcsStatus;
import com.enlopo.gitlinkproj.api.GitApi;
import com.enlopo.gitlinkproj.api.VcsApi;

import java.awt.*;

public class GetLineUrlAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile file = FileEditorManager.getInstance(project).getSelectedFiles()[0];
        VcsApi vcsApi = new GitApi(project, file);

        VcsStatus status = vcsApi.checkVcsStatus();
        if (status != VcsStatus.CLEAN) {
            Messages.showErrorDialog(project, "VCS Status: " + status, "Error");
            return;
        }

        Editor editor = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor();
        String filePath = FileEditorManager.getInstance(e.getProject()).getSelectedFiles()[0].getPath();
        // 获取当前行号，加1是因为API中行号从0开始
        int lineNumber = editor.getCaretModel().getLogicalPosition().line + 1;

        String url = vcsApi.getBaseUrl() + "/blob/" + vcsApi.getCurrentBranchName() + "/" +
                file.getPath().substring(vcsApi.getRepositoryRootPath().length() + 1).replace("\\", "/") +
                "#L" + lineNumber;

//        Messages.showInfoMessage(project, "Generated URL: " + url, "URL Generated");

        EventQueue.invokeLater(() -> {
            UrlDialog urlDialog = new UrlDialog(null, url);
            urlDialog.setVisible(true);
        });
    }
}

