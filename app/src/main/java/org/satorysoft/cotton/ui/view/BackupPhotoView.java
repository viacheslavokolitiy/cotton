package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.BackupPhotoComponent;
import org.satorysoft.cotton.di.mortar.BackupPresenter;
import org.satorysoft.cotton.ui.view.widget.RobotoButton;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
public class BackupPhotoView extends RelativeLayout {
    @Inject
    protected BackupPresenter backupPresenter;

    @FindView(R.id.toolbar_backup_data)
    protected Toolbar toolbar;
    @FindView(R.id.photo_grid)
    protected GridView photoGrid;
    @FindView(R.id.btn_backup_photos)
    protected RobotoButton buttonBackupPhotos;
    private Context context;

    public BackupPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        DaggerService.<BackupPhotoComponent>getDaggerComponent(context).inject(this);
    }

    public BackupPhotoView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        backupPresenter.loadNewPhotos(context, photoGrid);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        backupPresenter.takeView(this);
        photoGrid.setNumColumns(3);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        backupPresenter.dropView(this);
        ButterKnife.unbind(this);
    }

    @OnItemClick(R.id.photo_grid)
    public void onPhotoGridClick(int position){
        backupPresenter.selectPhotoForBackup(context, photoGrid, position);
    }

    @OnClick(R.id.btn_backup_photos)
    public void onBackupClick(){
        backupPresenter.backupPhotos(context);
    }
}
