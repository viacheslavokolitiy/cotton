package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.ScheduleSelectedEvent;
import org.satorysoft.cotton.di.component.mortar.ScheduledBackupComponent;
import org.satorysoft.cotton.di.mortar.ScheduledBackupPresenter;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.DaggerService;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 02.06.2015.
 */
public class ScheduledBackupView extends RelativeLayout {
    @Inject
    protected ScheduledBackupPresenter scheduledBackupPresenter;

    @FindView(R.id.checkbox_select_photo)
    protected CheckBox selectPhotoCategory;
    @FindView(R.id.checkbox_select_call_log)
    protected CheckBox selectCallLogCategory;
    @FindView(R.id.txt_setup_scheduled_backup)
    protected RobotoTextView setupScheduledBackup;
    @FindView(R.id.txt_scheduled_backup_date)
    protected RobotoTextView scheduledBackupDateTime;

    public ScheduledBackupView(Context context) {
        super(context);
    }

    public ScheduledBackupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<ScheduledBackupComponent>getDaggerComponent(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        scheduledBackupPresenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        scheduledBackupPresenter.dropView(this);
        ButterKnife.unbind(this);
    }

    @OnCheckedChanged(R.id.checkbox_select_photo)
    public void onSelectPhoto(boolean isSelected){
        scheduledBackupPresenter.selectPhotoCategory(isSelected);
    }

    @OnCheckedChanged(R.id.checkbox_select_call_log)
    public void onSelectCallHistory(boolean isSelected){
        scheduledBackupPresenter.selectCallHistoryCategory(isSelected);
    }

    @OnClick(R.id.txt_setup_scheduled_backup)
    public void onSetupScheduledBackup() {
        scheduledBackupPresenter.setUpScheduledBackup();
    }
}
