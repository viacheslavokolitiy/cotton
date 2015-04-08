package org.satorysoft.cotton.core.event;

import org.satorysoft.cotton.core.model.SelectedApplication;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
public class PopulateCardViewEvent {
    private final SelectedApplication selectedApplication;

    public PopulateCardViewEvent(SelectedApplication selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public SelectedApplication getSelectedApplication() {
        return selectedApplication;
    }
}
