package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.BackupComponent;
import org.satorysoft.cotton.di.mortar.BackupPresenter;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
public class BackupView extends RelativeLayout {
    @Inject
    protected BackupPresenter backupPresenter;

    @FindView(R.id.toolbar_backup_data)
    protected Toolbar toolbar;

    public BackupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<BackupComponent>getDaggerComponent(context).inject(this);
    }

    public BackupView(Context context) {
        super(context);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        backupPresenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        backupPresenter.dropView(this);
        ButterKnife.unbind(this);
    }
}
