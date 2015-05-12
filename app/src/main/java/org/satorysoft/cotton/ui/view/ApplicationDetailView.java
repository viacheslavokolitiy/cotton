package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.PopulateCardViewEvent;
import org.satorysoft.cotton.di.component.mortar.ApplicationDetailComponent;
import org.satorysoft.cotton.di.mortar.ApplicationDetailPresenter;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
public class ApplicationDetailView extends RelativeLayout {
    @Inject
    protected ApplicationDetailPresenter presenter;
    private Context context;

    @FindView(R.id.application_icon_detail)
    protected ImageView applicationLogo;
    @FindView(R.id.text_application_name_detail)
    protected RobotoTextView applicationName;
    @FindView(R.id.recycler_permissions)
    protected RecyclerView recyclerView;

    public ApplicationDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        DaggerService.<ApplicationDetailComponent>getDaggerComponent(context).inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void onEvent(PopulateCardViewEvent event){
        presenter.setApplicationDetail(event, applicationLogo, applicationName);
        presenter.setPermissions(recyclerView, event, context);
    }
}
