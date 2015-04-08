package org.satorysoft.cotton.di.mortar;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.satorysoft.cotton.core.event.PopulateCardViewEvent;
import org.satorysoft.cotton.ui.view.ApplicationDetailView;
import org.satorysoft.cotton.ui.view.RobotoTextView;
import org.satorysoft.cotton.util.IDrawableStateManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
@Singleton
public class ApplicationDetailPresenter extends ViewPresenter<ApplicationDetailView> implements IDrawableStateManager {

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

    @Override
    public byte[] convertToBytes(Drawable drawable) {
        return new byte[0];
    }

    @Override public Drawable restoreDrawable(byte[] bytes){
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
}
