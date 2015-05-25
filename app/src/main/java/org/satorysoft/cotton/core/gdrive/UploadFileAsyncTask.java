package org.satorysoft.cotton.core.gdrive;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.FileUploadFailedEvent;
import org.satorysoft.cotton.core.event.UploadSuccessfulEvent;
import org.satorysoft.cotton.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 23.05.2015.
 */
public class UploadFileAsyncTask extends APIAsyncTask<String, Void, List<Metadata>> {
    private final ArrayList<String> images;
    private final Context context;

    public UploadFileAsyncTask(Context context, ArrayList<String> images) {
        super(context);
        this.context = context;
        this.images = images;

    }

    @Override
    protected List<Metadata> doInBackgroundConnected(String[] params) {
        final List<Metadata> fileMetadataList = new ArrayList<>();

        for(String imageURL : images){
            DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                    .newDriveContents(getGoogleApiClient())
                    .await();
            if (!contentsResult.getStatus().isSuccess()) {
                return null;
            }


            File fileForUpload = new File(imageURL);
            final DriveContents originalContents = contentsResult.getDriveContents();
            OutputStream outputStream = originalContents.getOutputStream();

            try {
                InputStream fileInputStream = new FileInputStream(fileForUpload);
                byte[] buffer = new byte[Constants.BUFFER_SIZE];
                int length;
                int counter = 0;
                while((length = fileInputStream.read(buffer)) > 0){
                    ++counter;
                    outputStream.write(buffer, 0, length);
                }

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MetadataChangeSet originalMetadata = new MetadataChangeSet.Builder()
                    .setTitle(fileForUpload.getName())
                    .setMimeType("image/jpg").build();

            String encodedDriveId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.APPFOLDER_DRIVE_ID, null);
            if(!TextUtils.isEmpty(encodedDriveId)) {
                DriveFolder appFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId));
                DriveFolder.DriveFileResult fileResult = appFolder.createFile(
                        getGoogleApiClient(), originalMetadata, originalContents).await();

                if (!fileResult.getStatus().isSuccess()) {
                    return null;
                }

                DriveResource.MetadataResult metadataResult = fileResult.getDriveFile()
                        .getMetadata(getGoogleApiClient())
                        .await();

                if (!metadataResult.getStatus().isSuccess()) {
                    return null;
                }

                fileMetadataList.add(metadataResult.getMetadata());
            }
        }

        return fileMetadataList;
    }

    @Override
    protected void onPostExecute(List<Metadata> metadataList) {
        super.onPostExecute(metadataList);

        if (metadataList != null && metadataList.size() == 0){
            EventBus.getDefault().post(new FileUploadFailedEvent(context.getString(R.string.text_upload_failed_error)));
        } else {
            EventBus.getDefault().post(new UploadSuccessfulEvent(context.getString(R.string.text_upload_success)));
        }
    }
}
