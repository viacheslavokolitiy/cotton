package org.satorysoft.cotton.ui.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.PopulateCardViewEvent;
import org.satorysoft.cotton.core.event.UpdateApplicationListEvent;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.di.component.DaggerUIViewsComponent;
import org.satorysoft.cotton.di.component.UIViewsComponent;
import org.satorysoft.cotton.di.component.mortar.ApplicationDetailComponent;
import org.satorysoft.cotton.di.module.UIViewsModule;
import org.satorysoft.cotton.di.mortar.ApplicationDetailPresenter;
import org.satorysoft.cotton.ui.view.widget.RobotoButton;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.DaggerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;

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
    @FindView(R.id.btn_trust_application)
    protected RobotoButton trustButton;
    @FindView(R.id.btn_delete_application)
    protected RobotoButton deleteButton;
    private UIViewsComponent uiComponent;
    private MaterialDialog materialDialog;

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

    @OnClick(R.id.btn_delete_application)
    public void onDelete(){
        Cursor cursor = context.getContentResolver().query(ScannedApplicationContract.CONTENT_URI,
                null, ScannedApplicationContract.APPLICATION_NAME + "=?",
                new String[]{applicationName.getText().toString()}, null);
        String packageName = "";
        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            do {
                packageName = cursor.getString(cursor.getColumnIndex(ScannedApplicationContract.PACKAGE_NAME));
            } while (cursor.moveToNext());
        }

        cursor.close();

        if(!TextUtils.isEmpty(packageName)){
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        }
    }

    @OnClick(R.id.btn_trust_application)
    public void onTrust(){
        this.uiComponent = DaggerUIViewsComponent.builder().uIViewsModule(new UIViewsModule(context)).build();
        this.materialDialog = uiComponent.getMaterialDialog();
        materialDialog.setTitle("Trust this application ?")
                .setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        trustApplication(applicationName);
                        materialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                })
                .setMessage("This action will put application to trusted applications list. " +
                        "Do you really want this ?")
                .show();
    }

    private void trustApplication(RobotoTextView applicationNameView) {
        String applicationName = applicationNameView.getText().toString();
        ContentValues values = new ContentValues();
        values.put(ScannedApplicationContract.APPLICATION_RISK_RATE, 0.0);
        context.getContentResolver().update(ScannedApplicationContract.CONTENT_URI, values,
                ScannedApplicationContract.APPLICATION_NAME + "=?", new String[]{applicationName});
        EventBus.getDefault().post(new UpdateApplicationListEvent());
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
