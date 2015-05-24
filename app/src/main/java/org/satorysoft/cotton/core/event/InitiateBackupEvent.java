package org.satorysoft.cotton.core.event;

import java.util.List;

/**
 * Created by viacheslavokolitiy on 22.05.2015.
 */
public class InitiateBackupEvent {
    private final List<String> selectedImages;
    public InitiateBackupEvent(List<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public List<String> getSelectedImages() {
        return selectedImages;
    }
}
