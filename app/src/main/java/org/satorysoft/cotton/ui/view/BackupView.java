package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.BackupComponent;
import org.satorysoft.cotton.di.mortar.BackupPresenter;
import org.satorysoft.cotton.ui.view.widget.RobotoButton;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 14.05.2015.
 */
public class BackupView extends RelativeLayout {
    @FindView(R.id.toolbar_backup)
    protected Toolbar backupViewToolbar;
    @FindView(R.id.btn_google_drive_login)
    protected RobotoButton btnGoogleDriveLogin;
    private Context context;
    @Inject
    protected BackupPresenter backupPresenter;

    public BackupView(Context context) {
        super(context);
    }

    public BackupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        DaggerService.<BackupComponent>getDaggerComponent(context).inject(this);
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
        ButterKnife.unbind(this);
        backupPresenter.dropView(this);
    }
}
