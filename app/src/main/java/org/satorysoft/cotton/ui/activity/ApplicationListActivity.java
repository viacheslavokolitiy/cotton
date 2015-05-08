package org.satorysoft.cotton.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.DrawerListAdapter;
import org.satorysoft.cotton.core.event.SortAppsByNameEvent;
import org.satorysoft.cotton.core.event.SortAppsByRiskEvent;
import org.satorysoft.cotton.core.model.DrawerItem;
import org.satorysoft.cotton.di.component.DaggerUIViewsComponent;
import org.satorysoft.cotton.di.component.UIViewsComponent;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;
import org.satorysoft.cotton.di.module.UIViewsModule;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.ui.drawable.ArrowDrawable;
import org.satorysoft.cotton.ui.drawable.DrawerToggle;
import org.satorysoft.cotton.ui.view.FloatingActionButton;
import org.satorysoft.cotton.ui.view.RobotoTextView;
import org.satorysoft.cotton.util.DaggerService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static org.satorysoft.cotton.util.DaggerService.createComponent;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListActivity extends MortarActivity implements View.OnClickListener {

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
        this.uiComponent = DaggerUIViewsComponent.builder().uIViewsModule(new UIViewsModule(this)).build();
        this.materialDialog = uiComponent.getMaterialDialog();
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

    private View createCustomDialogView(){
        LayoutInflater inflater = getLayoutInflater();
        return inflater.inflate(R.layout.search_video_dialog_view, null, false);
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

    @Override
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void onClick(View view) {
        //TODO: showSeachDialog();
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
        } else if(item.getItemId() == R.id.action_settings){
            EventBus.getDefault().post(new SortAppsByRiskEvent());
        } else if(item.getItemId() == R.id.action_sort_by_name){
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

    @Override
    public void setCustomActionBarTitle(String title) {
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_action_bar_title, null);
        ((RobotoTextView)v.findViewById(R.id.text_custom_action_bar_title)).setText(title);
        getSupportActionBar().setCustomView(v);
    }

    private void  hideActionButtonOnScroll(){
        RecyclerView view = ButterKnife.findById(this, R.id.recycler);
        if(view != null){
            view.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if(newState == MotionEvent.ACTION_UP){
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
