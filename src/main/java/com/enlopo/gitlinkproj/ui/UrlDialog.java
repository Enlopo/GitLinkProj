package com.enlopo.gitlinkproj.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class UrlDialog extends JDialog {
    public UrlDialog(Frame owner, String url) {
        super(owner, "Git URL", true);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea urlArea = new JTextArea(url);
        urlArea.setWrapStyleWord(true);
        urlArea.setLineWrap(true);
        urlArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(urlArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton copyButton = new JButton("Copy URL");
        JButton openButton = new JButton("Open URL");
        JButton helpButton = new JButton("HELP!!!!");

        copyButton.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(url), null);
            showToast("URL Copied to Clipboard");
        });

        openButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to open URL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        helpButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("weixin://message?username=Yudeweixin301"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to open URL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(copyButton);
        buttonPanel.add(openButton);
        buttonPanel.add(helpButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showToast(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(this, "Notification");
        dialog.setModal(false);  // Set dialog to non-modal to act as a toast
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }, 2000);  // Display the toast for 2 seconds
        dialog.setVisible(true);
    }
}
