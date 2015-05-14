package org.satorysoft.cotton.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.SortAppsByNameEvent;
import org.satorysoft.cotton.core.event.SortAppsByRiskEvent;
import org.satorysoft.cotton.core.event.UpdateApplicationListEvent;
import org.satorysoft.cotton.core.model.DrawerItem;
import org.satorysoft.cotton.di.component.DaggerUIViewsComponent;
import org.satorysoft.cotton.di.component.UIViewsComponent;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;
import org.satorysoft.cotton.di.module.UIViewsModule;
import org.satorysoft.cotton.di.mortar.ApplicationListPresenter;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.ui.drawable.ArrowDrawable;
import org.satorysoft.cotton.ui.drawable.DrawerToggle;
import org.satorysoft.cotton.ui.view.widget.FloatingActionButton;

import java.util.ArrayList;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListActivity extends MortarActivity<ApplicationListComponent> implements View.OnClickListener {
    private static final int SCHEDULED_BACKUP = 2;
    private static final int BACKUP = 1;
    private ArrowDrawable mDrawerArrow;
    private DrawerToggle mActionBarDrawerToggle;
    private ArrayList<DrawerItem> mDrawerItems;
    private DrawerLayout containingView;
    private ListView leftDrawer;
    private MenuInflater mInflater;
    private UIViewsComponent uiComponent;
    private MaterialDialog materialDialog;
    private FloatingActionButton floatingActionButton;

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        EventBus.getDefault().register(this);
        this.uiComponent = DaggerUIViewsComponent.builder().uIViewsModule(new UIViewsModule(this)).build();
        this.materialDialog = uiComponent.getMaterialDialog();

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
                                startActivity(new Intent(ApplicationListActivity.this, BackupActivity.class));
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

        floatingActionButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.mipmap.ic_action_search))
                .withButtonColor(getResources().getColor(R.color.md_red_500))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        floatingActionButton.setOnClickListener(this);

        hideActionButtonOnScroll();
    }

    private void showSearchDialog() {
        materialDialog.setTitle("Search application")
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                        floatingActionButton.showFloatingActionButton();

                    }
                })
                .setNegativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                        floatingActionButton.showFloatingActionButton();
                    }
                });
        materialDialog.setMessage("");
        materialDialog.setContentView(createCustomDialogView());
        floatingActionButton.hideFloatingActionButton();
        materialDialog.show();
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
    public void onClick(View view) {
        //TODO: showSeachDialog();
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

    private void hideActionButtonOnScroll() {
        RecyclerView view = ButterKnife.findById(this, R.id.recycler);
        if (view != null) {
            view.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == MotionEvent.ACTION_UP) {
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
