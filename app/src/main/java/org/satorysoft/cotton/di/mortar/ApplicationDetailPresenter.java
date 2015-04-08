package org.satorysoft.cotton.di.mortar;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import org.satorysoft.cotton.adapter.PermissionListAdapter;
import org.satorysoft.cotton.core.event.PopulateCardViewEvent;
import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.DaggerCoreComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.ui.animator.SlideInFromLeftItemAnimator;
import org.satorysoft.cotton.ui.view.ApplicationDetailView;
import org.satorysoft.cotton.ui.view.RobotoTextView;
import org.satorysoft.cotton.util.IDrawableStateManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
@Singleton
public class ApplicationDetailPresenter extends ViewPresenter<ApplicationDetailView> implements IDrawableStateManager {

    private CoreComponent mCoreComponent;
    private PackageManager mPackageManager;

    @Inject
    public ApplicationDetailPresenter(){

    }

    public void setApplicationDetail(PopulateCardViewEvent event, ImageView applicationLogo,
                                     RobotoTextView applicationName) {
        byte[] icon = event.getSelectedApplication().getIcon();
        String name = event.getSelectedApplication().getTitle();
        applicationLogo.setImageDrawable(restoreDrawable(icon));
        applicationName.setText(name);
    }

    public void setPermissions(RecyclerView recyclerView, PopulateCardViewEvent event, Context context){
        this.mCoreComponent = DaggerCoreComponent.builder().coreModule(new CoreModule(context)).build();
        this.mPackageManager = mCoreComponent.getPackageManager();
        PermissionListAdapter adapter = mCoreComponent.getPermissionsAdapter();
        List<String> permissions = Arrays.asList(event.getSelectedApplication().getPermissions());
        for(String permission : permissions){
            try {
                PermissionInfo info = mPackageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);
                String permissionName;
                if(info.loadDescription(mPackageManager) != null) {
                    permissionName = info.loadDescription(mPackageManager).toString();
                    adapter.addItem(permissionName);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInFromLeftItemAnimator(recyclerView));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public byte[] convertToBytes(Drawable drawable) {
        return new byte[0];
    }

    @Override public Drawable restoreDrawable(byte[] bytes){
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
}
