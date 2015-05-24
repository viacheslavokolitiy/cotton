package org.satorysoft.cotton.di.mortar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.GridView;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;

import org.satorysoft.cotton.adapter.PhotoGridAdapter;
import org.satorysoft.cotton.core.event.DeclineBackupEvent;
import org.satorysoft.cotton.core.event.InitiateBackupEvent;
import org.satorysoft.cotton.core.gdrive.UploadFileAsyncTask;
import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.DaggerCoreComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.ui.view.BackupPhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
@Singleton
public class BackupPresenter extends ViewPresenter<BackupPhotoView> {
    private static final int EMPTY_LIST_SIZE = 0;
    private final ArrayList<String> selectedImages;
    private CoreComponent coreComponent;
    private PhotoGridAdapter photoAdapter;

    @Inject
    public BackupPresenter(){
        selectedImages = new ArrayList<>();
    }

    public void loadNewPhotos(final Context context, GridView photoGrid) {
        this.coreComponent = DaggerCoreComponent.builder().coreModule(new CoreModule(context)).build();
        this.photoAdapter = coreComponent.getPhotoGridAdapter();
        photoGrid.setAdapter(photoAdapter);

        new AsyncTask<Void, Integer, List<String>>() {
            public ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(false);
                progressDialog.show();
            }

            @Override
            protected List<String> doInBackground(Void... voids) {
                final List<String> imageURLs = getImageURLS(context);

                return imageURLs;
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                for(String imageURI : result){
                    File targetFile = new File(imageURI);
                    photoAdapter.addImage(targetFile.getAbsolutePath());
                }
            }
        }.execute();
    }

    private List<String> getImageURLS(Context context) {
        final List<String> imageURLs = new ArrayList<>();
        String[] columns = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            do {
                String imageURL = cursor.getString(0);
                imageURLs.add(imageURL);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return imageURLs;
    }

    public void selectPhotoForBackup(Context context, GridView photoGrid, int position) {
        String imageURL = getImageURLS(context).get(position);
        if(!selectedImages.contains(imageURL)){
            selectedImages.add(imageURL);
        } else {
            selectedImages.remove(imageURL);
            selectedImages.trimToSize();
        }

        highlightSelectedImage(photoGrid, position);

        if(selectedImages.size() > EMPTY_LIST_SIZE){
            EventBus.getDefault().post(new InitiateBackupEvent(selectedImages));
        } else {
            EventBus.getDefault().post(new DeclineBackupEvent());
        }
    }

    private void highlightSelectedImage(GridView photoGrid, int position) {
        //TODO: Implement CAB pattern
    }

    public void backupPhotos(Context context) {
        if(selectedImages.size() > 0){
            new UploadFileAsyncTask(context, selectedImages).execute();
        }
    }
}
