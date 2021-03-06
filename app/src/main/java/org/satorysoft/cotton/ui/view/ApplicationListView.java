package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.SortAppsByNameEvent;
import org.satorysoft.cotton.core.event.SortAppsByRiskEvent;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;
import org.satorysoft.cotton.di.mortar.ApplicationListPresenter;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListView extends RelativeLayout {
    @Inject
    ApplicationListPresenter applicationListPresenter;

    @FindView(R.id.recycler)
    protected RecyclerView recyclerView;
    @FindView(R.id.toolbar)
    protected Toolbar toolbar;

    private Context context;

    public ApplicationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        DaggerService.<ApplicationListComponent>getDaggerComponent(context).inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
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

    public void onEvent(SortAppsByRiskEvent event){
        applicationListPresenter.sortListByRisk(recyclerView, context);
    }

    public void onEvent(SortAppsByNameEvent event){
        applicationListPresenter.sortListByApplicationName(recyclerView, context);
    }
}
