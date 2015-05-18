package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.BackupComponent;
import org.satorysoft.cotton.di.mortar.BackupPresenter;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnItemClick;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
public class BackupView extends RelativeLayout {
    @Inject
    protected BackupPresenter backupPresenter;

    @FindView(R.id.toolbar_backup_data)
    protected Toolbar toolbar;
    @FindView(R.id.photo_grid)
    protected GridView photoGrid;
    private Context context;

    public BackupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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
        photoGrid.setNumColumns(3);
        backupPresenter.loadNewPhotos(context, photoGrid);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        backupPresenter.dropView(this);
        ButterKnife.unbind(this);
    }

    @OnItemClick(R.id.photo_grid)
    public void onPhotoGridClick(int position){
        Toast.makeText(context, Integer.toString(position), Toast.LENGTH_LONG).show();
    }
}
