package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.di.component.mortar.ScheduledBackupComponent;
import org.satorysoft.cotton.di.mortar.ScheduledBackupPresenter;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by viacheslavokolitiy on 02.06.2015.
 */
public class ScheduledBackupView extends RelativeLayout {
    @Inject
    protected ScheduledBackupPresenter scheduledBackupPresenter;

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
    }
}
