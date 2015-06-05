package org.satorysoft.cotton.di.mortar;

import android.content.Context;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.SaveScheduleEvent;
import org.satorysoft.cotton.core.event.ScheduleSelectedEvent;
import org.satorysoft.cotton.core.event.SetupScheduledBackupFailedEvent;
import org.satorysoft.cotton.core.event.ShowSelectScheduledBackupScheduleEvent;
import org.satorysoft.cotton.ui.view.ScheduledBackupView;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.DayType;
import org.satorysoft.slidedaytimepicker.SlideDayTimeListener;
import org.satorysoft.slidedaytimepicker.SlideDayTimePicker;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 02.06.2015.
 */
@Singleton
public class ScheduledBackupPresenter extends ViewPresenter<ScheduledBackupView> {
    private boolean isPhotoCategorySelected;
    private boolean isCallLogSelected;
    private boolean scheduleCreated;

    @Inject
    public ScheduledBackupPresenter() {
        EventBus.getDefault().register(this);
    }

    public void selectPhotoCategory(boolean isPhotoCategorySelected) {
        this.isPhotoCategorySelected = isPhotoCategorySelected;
    }

    public void selectCallHistoryCategory(boolean isSelected) {
        this.isCallLogSelected = isSelected;
    }

    public void setUpScheduledBackup() {
        if (!isCallLogSelected && !isPhotoCategorySelected) {
            EventBus.getDefault().post(new SetupScheduledBackupFailedEvent());
            return;
        }

        EventBus.getDefault().post(new ShowSelectScheduledBackupScheduleEvent());
    }

    public void onEvent(ScheduleSelectedEvent event){
        Calendar calendar = event.getCalendar();
        RobotoTextView scheduleView = ButterKnife.findById(getView(), R.id.txt_scheduled_backup_date);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String date = DayType.getDayType(day).toString();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        if(minute < 10){
            scheduleView.setText(date + ", " + Integer.toString(hour) + ":" + "0" + Integer.toString(minute));
            scheduleCreated = true;
        } else {
            scheduleView.setText(date + ", " + Integer.toString(hour) + ":" + Integer.toString(minute));
            scheduleCreated = true;
        }
    }

    public void onEvent(SaveScheduleEvent event){
        Context context = event.getContext();
    }
}
