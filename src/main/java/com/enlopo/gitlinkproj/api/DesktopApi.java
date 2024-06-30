package com.enlopo.gitlinkproj.api;

import java.awt.Desktop;
import java.net.URI;

public class DesktopApi {
    public static void openUrl(String url) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(url);
                desktop.browse(uri);
            } else {
                throw new UnsupportedOperationException("Desktop doesn't support the browse action");
            }
        } else {
            throw new UnsupportedOperationException("Desktop is not supported on this platform");
        }
    }
}
