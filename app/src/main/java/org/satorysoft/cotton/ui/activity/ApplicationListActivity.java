package org.satorysoft.cotton.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.DrawerListAdapter;
import org.satorysoft.cotton.core.model.DrawerItem;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;
import org.satorysoft.cotton.ui.drawable.ArrowDrawable;
import org.satorysoft.cotton.ui.drawable.DrawerToggle;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortar.dagger2support.DaggerService;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static mortar.dagger2support.DaggerService.createComponent;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListActivity extends ActionBarActivity {

    private ArrowDrawable mDrawerArrow;
    private DrawerToggle mActionBarDrawerToggle;
    private ArrayList<DrawerItem> mDrawerItems;
    private DrawerLayout containingView;
    private ListView leftDrawer;
    private MenuInflater mInflater;

    @Override
    public Object getSystemService(String name) {
        MortarScope activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, createComponent(ApplicationListComponent.class))
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        containingView = (DrawerLayout) findViewById(R.id.drawer_view);
        leftDrawer = (ListView) containingView.findViewById(R.id.left_drawer);

        mDrawerArrow = new ArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mActionBarDrawerToggle = new DrawerToggle(this, containingView,
                mDrawerArrow, R.string.app_name,
                R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        containingView.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mDrawerItems = new ArrayList<>();
        DrawerItem item = new DrawerItem();
        item.setDrawerItemTitle("TODO");
        mDrawerItems.add(item);

        DrawerListAdapter adapter = new DrawerListAdapter(this, mDrawerItems);
        EventBus.getDefault().post(new PopulateDrawerEvent(adapter));

        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        mActionBarDrawerToggle.setAnimateEnabled(true);
                        containingView.closeDrawer(leftDrawer);
                        mDrawerArrow.setProgress(1f);
                        break;
                }

            }
        });

        containingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        if(mDrawerArrow.getProgress() == 1f){
                            containingView.closeDrawer(leftDrawer);
                        }
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }

    private String getScopeName() {
        return getClass().getName();
    }

    public static class PopulateDrawerEvent {
        private final DrawerListAdapter adapter;

        public PopulateDrawerEvent(DrawerListAdapter adapter) {
            this.adapter = adapter;
        }

        public DrawerListAdapter getAdapter() {
            return adapter;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (containingView.isDrawerOpen(leftDrawer)) {
                containingView.closeDrawer(leftDrawer);
            } else {
                containingView.openDrawer(leftDrawer);
            }
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
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean drawerVisible = containingView.isDrawerVisible(leftDrawer);
        menu.findItem(R.id.action_settings).setVisible(!drawerVisible);
        return super.onPrepareOptionsMenu(menu);
    }
}
