package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.SaveScheduleEvent;
import org.satorysoft.cotton.core.event.ScheduleSelectedEvent;
import org.satorysoft.cotton.core.event.SetupScheduledBackupFailedEvent;
import org.satorysoft.cotton.core.event.ShowSelectScheduledBackupScheduleEvent;
import org.satorysoft.cotton.di.component.mortar.ScheduledBackupComponent;
import org.satorysoft.cotton.di.mortar.ScheduledBackupPresenter;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.ui.view.ScheduledBackupView;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.slidedaytimepicker.SlideDayTimeListener;
import org.satorysoft.slidedaytimepicker.SlideDayTimePicker;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 14.05.2015.
 */
public class ScheduledBackupActivity extends MortarActivity<ScheduledBackupComponent> {

    @Override
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_backup);
        EventBus.getDefault().register(this);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar_scheduled_backup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCustomActionBarTitle(getString(R.string.text_scheduled_backup_toolbar));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_save_schedule:
                EventBus.getDefault().post(new SaveScheduleEvent(this));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(SetupScheduledBackupFailedEvent event) {
        Toast.makeText(getApplicationContext(), R.string.text_toast_setup_scheduled_backup_error, Toast.LENGTH_SHORT).show();
    }

    public void onEvent(ShowSelectScheduledBackupScheduleEvent event) {
        final SlideDayTimeListener listener = new SlideDayTimeListener() {

            @Override
            public void onDayTimeSet(int day, int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, day);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);

                EventBus.getDefault().post(new ScheduleSelectedEvent(calendar));
            }

            @Override
            public void onDayTimeCancel() {

            }
        };

        new SlideDayTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDay(1)
                .setInitialHour(0)
                .setInitialMinute(00)
                .build()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_set_scheduled_backup, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
