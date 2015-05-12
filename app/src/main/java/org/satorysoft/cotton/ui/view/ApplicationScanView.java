package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.ApplicationScanComponent;
import org.satorysoft.cotton.di.mortar.ApplicationScan;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class ApplicationScanView extends RelativeLayout {
    @Inject
    ApplicationScan.ApplicationScanPresenter applicationScanPresenter;

    @FindView(R.id.arc_scan_progress)
    protected ArcProgress progress;
    @FindView(R.id.text_progress_placeholder)
    protected RobotoTextView progressDescription;

    public ApplicationScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<ApplicationScanComponent>getDaggerComponent(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        applicationScanPresenter.launchApplicationScan(getContext(), progress);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        applicationScanPresenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        applicationScanPresenter.dropView(this);
    }
}
