package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ListView;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.SortAppsByNameEvent;
import org.satorysoft.cotton.core.event.SortAppsByRiskEvent;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;
import org.satorysoft.cotton.di.mortar.ApplicationListPresenter;
import org.satorysoft.cotton.ui.activity.ApplicationListActivity;
import org.satorysoft.cotton.util.DaggerServiceCompat;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListView extends DrawerLayout {
    @Inject
    ApplicationListPresenter applicationListPresenter;

    @InjectView(R.id.recycler)
    protected RecyclerView recyclerView;
    @InjectView(R.id.left_drawer)
    protected ListView leftDrawer;

    private Context context;

    public ApplicationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        DaggerServiceCompat.<ApplicationListComponent>getDaggerComponent(context).inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        applicationListPresenter.populateListView(recyclerView, context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        applicationListPresenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        applicationListPresenter.dropView(this);
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(ApplicationListActivity.PopulateDrawerEvent event){
        if(event.getAdapter() != null){
            leftDrawer.setAdapter(event.getAdapter());
        }
    }

    public void onEvent(SortAppsByRiskEvent event){
        applicationListPresenter.sortListByRisk(recyclerView, context);
    }

    public void onEvent(SortAppsByNameEvent event){
        applicationListPresenter.sortListByApplicationName(recyclerView, context);
    }
}
