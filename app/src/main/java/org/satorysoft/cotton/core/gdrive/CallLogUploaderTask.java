package org.satorysoft.cotton.core.gdrive;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import org.json.JSONException;
import org.json.JSONObject;
import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.FileUploadFailedEvent;
import org.satorysoft.cotton.core.event.UploadSuccessfulEvent;
import org.satorysoft.cotton.core.model.CallLogData;
import org.satorysoft.cotton.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 01.06.2015.
 */
public class CallLogUploaderTask extends APIAsyncTask<Void, Integer, List<Metadata>> {
    private final Context context;
    private ProgressDialog dialog;
    private DriveFolder backupCallLogFolder;
    private ArrayList<DriveFolder> backupCallLogFolders = new ArrayList<>();
    private ArrayList<JSONObject> jsonObjects = new ArrayList<>();
    private FileWriter fileWriter;

    public CallLogUploaderTask(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.text_upload_calls));
        dialog.show();
    }

    @Override
    protected List<Metadata> doInBackgroundConnected(Void[] params) {
        List<CallLogData> callLogDataList = queryForCallHistory(new Date());
        final List<Metadata> fileMetadataList = new ArrayList<>();
        final String encodedDriveId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.APPFOLDER_DRIVE_ID, null);
        String encodedCallLogFolderId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.CALL_LOG_FOLDER_ID, null);

        if(TextUtils.isEmpty(encodedCallLogFolderId)){
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.folder")).build();
            if(!TextUtils.isEmpty(encodedDriveId)){
                Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId))
                              .queryChildren(getGoogleApiClient(), query)
                              .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                  @Override
                                  public void onResult(DriveApi.MetadataBufferResult result) {
                                      for(Metadata metadata : result.getMetadataBuffer()){
                                          if(!metadata.isTrashed()){
                                              String title = metadata.getTitle();
                                              if(title.equals(context.getString(R.string.text_backup_call_log_name))){
                                                  backupCallLogFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), metadata.getDriveId());
                                                  backupCallLogFolders.add(backupCallLogFolder);
                                              }
                                          }
                                      }

                                      if(backupCallLogFolders.size() == 0){
                                          DriveFolder appFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedDriveId));
                                          backupCallLogFolder = createFolderWithName(context,
                                                  appFolder,
                                                  context.getString(R.string.text_call_log_folder_name),
                                                  Constants.CALL_LOG_FOLDER_ID);
                                      }
                                  }
                              });
            }
        } else {
            backupCallLogFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), DriveId.decodeFromString(encodedCallLogFolderId));
        }


       File jsonFile = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"
                 + Long.toString(new Date().getTime()) + ".json");


        for(CallLogData callLogData : callLogDataList) {
            DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                    .newDriveContents(getGoogleApiClient())
                    .await();
            if (!contentsResult.getStatus().isSuccess()) {
                return null;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(CallLog.Calls._ID, callLogData.getId());
                jsonObject.put(CallLog.Calls.NUMBER, callLogData.getPhoneNumber());
                jsonObject.put(CallLog.Calls.CACHED_NAME, callLogData.getContactName());
                jsonObjects.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            fileWriter = new FileWriter(jsonFile);
            for(JSONObject jsonObject : jsonObjects){
                fileWriter.write(jsonObject.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi
                .newDriveContents(getGoogleApiClient())
                .await();

        final DriveContents originalContents = contentsResult.getDriveContents();
        OutputStream outputStream = originalContents.getOutputStream();

        try {
            InputStream fileInputStream = new FileInputStream(jsonFile);
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

        if (!contentsResult.getStatus().isSuccess()) {
            return null;
        }

        MetadataChangeSet originalMetadata = new MetadataChangeSet.Builder()
                .setTitle(jsonFile.getName())
                .setMimeType("text/plain").build();

        if(!TextUtils.isEmpty(encodedDriveId)) {

            DriveFolder.DriveFileResult fileResult = backupCallLogFolder.createFile(
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
        return fileMetadataList;
    }

    @Override
    protected void onPostExecute(List<Metadata> metadataList) {
        super.onPostExecute(metadataList);

        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }

        Toast.makeText(context, "Call history has backed up successfully", Toast.LENGTH_SHORT).show();
    }

    private List<CallLogData> queryForCallHistory(Date date){
        List<CallLogData> callLogDataList = new ArrayList<>();
        Cursor callsCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null,
                CallLog.Calls.DATE + "<?",
                new String[] { String.valueOf(date.getTime())},
                CallLog.Calls.NUMBER + " asc");
        if(callsCursor != null && callsCursor.getCount() > 0 && callsCursor.moveToFirst()){
            do {
                CallLogData data = new CallLogData();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                        && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
                    String id = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls._ID));
                    String phoneNumber = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String contactName = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    data.setId(id);
                    data.setPhoneNumber(phoneNumber);
                    data.setContactName(contactName);
                    callLogDataList.add(data);
                }

            } while (callsCursor.moveToNext());
        }

        return callLogDataList;
    }
}
