package org.satorysoft.cotton.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.SelectedApplicationEvent;
import org.satorysoft.cotton.core.event.SortAppsByNameEvent;
import org.satorysoft.cotton.core.event.SortAppsByRiskEvent;
import org.satorysoft.cotton.core.event.UpdateApplicationListEvent;
import org.satorysoft.cotton.di.component.DaggerRootComponent;
import org.satorysoft.cotton.di.component.RootComponent;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;
import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.di.mortar.ApplicationListPresenter;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.util.Constants;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListActivity extends MortarActivity<ApplicationListComponent> {
    private static final int SCHEDULED_BACKUP = 2;
    private static final int BACKUP = 1;
    private MenuInflater mInflater;
    private RootComponent rootComponent;

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        EventBus.getDefault().register(this);
        this.rootComponent = DaggerRootComponent.builder().rootModule(new RootModule(this)).build();

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Backup data"),
                        new PrimaryDrawerItem().withName("Scheduled backup"),
                        new PrimaryDrawerItem().withName("Restore data")
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id, IDrawerItem drawerItem) {
                        switch (position) {
                            case BACKUP:
                                if (rootComponent.getBooleanPreference().get(Constants.GOOGLE_DRIVE_AUTH_SUCCESS)) {
                                    startActivity(new Intent(ApplicationListActivity.this, BackupActivity.class));
                                } else {
                                    startActivity(new Intent(ApplicationListActivity.this, GoogleDriveAuthActivity.class));
                                }

                                break;
                            case SCHEDULED_BACKUP:
                                startActivity(new Intent(ApplicationListActivity.this, ScheduledBackupActivity.class));
                                break;
                        }
                    }
                })
                .build();

        result.setSelection(0);


        setCustomActionBarTitle(getString(R.string.text_action_bar_app_title));
    }

    private View createCustomDialogView() {
        LayoutInflater inflater = getLayoutInflater();
        return inflater.inflate(R.layout.search_video_dialog_view, null, false);
    }

    public void onEvent(UpdateApplicationListEvent event) {
        final RecyclerView view = ButterKnife.findById(this, R.id.recycler);
        if (view != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ApplicationListPresenter().populateListView(view, getApplicationContext());
                }
            });
        }
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
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            EventBus.getDefault().post(new SortAppsByRiskEvent());
        } else if (item.getItemId() == R.id.action_sort_by_name) {
            EventBus.getDefault().post(new SortAppsByNameEvent());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setCustomActionBarTitle(String title) {
        super.setCustomActionBarTitle(title);
    }

    public void onEvent(SelectedApplicationEvent event){
        Intent selectedIntent = event.getIntent();
        startActivity(selectedIntent);
    }
}
