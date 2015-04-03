package org.satorysoft.cotton.core.event;

import org.satorysoft.cotton.core.model.ScannedApplication;

import java.util.List;

/**
 * Created by viacheslavokolitiy on 02.04.2015.
 */
public class CompletedScanEvent {
    private final List<ScannedApplication> mScannedApplications;

    public CompletedScanEvent(List<ScannedApplication> scannedApplications) {
        this.mScannedApplications = scannedApplications;
    }

    public List<ScannedApplication> getScannedApplications() {
        return mScannedApplications;
    }
}
