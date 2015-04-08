package org.satorysoft.cotton.core.scanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.satorysoft.cotton.core.event.CompletedScanEvent;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.Dagger_CoreComponent;
import org.satorysoft.cotton.di.component.Dagger_RootComponent;
import org.satorysoft.cotton.di.component.RootComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.util.BooleanPreference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 02.04.2015.
 */
public class ApplicationScanner extends AsyncTask<Void, Integer, List<ScannedApplication>> {
    public static final CharSequence ARRAY_DIVIDER = "__,__";
    private final CoreComponent mCoreComponent;
    private final PackageManager mPackageManager;
    private final ArcProgress mProgress;
    private final Context mContext;
    private final RootComponent rootComponent;

    @Inject
    public ApplicationScanner(Context context, ArcProgress progress){
        this.mContext = context;
        this.mProgress = progress;
        this.mCoreComponent = Dagger_CoreComponent.builder().coreModule(new CoreModule(mContext)).build();
        this.rootComponent = Dagger_RootComponent.builder().rootModule(new RootModule(mContext)).build();
        this.mPackageManager = mCoreComponent.getPackageManager();
    }

    @Override
    protected List<ScannedApplication> doInBackground(Void... voids) {
        //scanning progress
        int progress = 0;
        //list for storing scanned applications
        final List<ScannedApplication> scannedApplications = new ArrayList<>();

        for(ApplicationInfo applicationInfo : getInstalledApplications()){
            InstalledApplication installedApplication = new InstalledApplication();
            ScannedApplication scannedApplication = new ScannedApplication();
            if(!isSystemApplication(applicationInfo)){
                String applicationName = applicationInfo.loadLabel(mPackageManager).toString();
                String packageName = applicationInfo.packageName;
                Drawable applicationIcon = applicationInfo.loadIcon(mPackageManager);
                byte[] imageRepresentation = convertDrawable(applicationIcon);

                try {
                    PackageInfo permissionsInfo = mPackageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
                    String[] applicationPermissions = permissionsInfo.requestedPermissions;
                    double applicationRiskRate = (double)evaluateApplicationRisk(applicationPermissions);
                    installedApplication.setApplicationName(applicationName);
                    installedApplication.setPackageName(packageName);
                    installedApplication.setApplicationIconBytes(imageRepresentation);
                    installedApplication.setApplicationRiskRate(applicationRiskRate);
                    installedApplication.setApplicationPermissions(applicationPermissions);
                    scannedApplication.setInstalledApplication(installedApplication);
                    scannedApplication.setScanDate(System.currentTimeMillis());
                    scannedApplications.add(scannedApplication);

                    synchronized (this){
                        try {
                            wait(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        publishProgress(progress++);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return scannedApplications;
    }

    private double evaluateApplicationRisk(String[] applicationPermissions) {
        if(applicationPermissions == null){
            applicationPermissions = new String[]{};
        }

        List<String> dangerousPermissions = getDangerousPermissions();
        ArrayList<String> foundPermissions = new ArrayList<>();
        ArrayList<String> safePermissions = new ArrayList<>();
        for(String permissionName : applicationPermissions){
            if(dangerousPermissions.contains(permissionName)){
                foundPermissions.add(permissionName);
            } else {
                safePermissions.add(permissionName);
            }
        }

        double riskRate;

        if(safePermissions.size() > 0 || foundPermissions.size() > 0){
            riskRate = (double)safePermissions.size() / (safePermissions.size() + foundPermissions.size());
            safePermissions.clear();
            foundPermissions.clear();
            safePermissions.trimToSize();
            foundPermissions.trimToSize();
        } else {
            riskRate = 0.0;
        }

        return riskRate;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgress.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<ScannedApplication> scannedApplications) {
        super.onPostExecute(scannedApplications);
        saveScanResultToDatabase(scannedApplications);
        mProgress.setProgress(100);
        EventBus.getDefault().post(new CompletedScanEvent());
        BooleanPreference preference = rootComponent.getBooleanPreference();
        preference.set("firstrun");
    }

    private boolean isSystemApplication(ApplicationInfo applicationInfo){
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

    }

    private List<ApplicationInfo> getInstalledApplications(){
        return mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    private List<String> getDangerousPermissions(){
        return mCoreComponent.getPermissionList().getHighRiskPermissions();
    }

    private byte[] convertDrawable(Drawable drawable){
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void saveScanResultToDatabase(List<ScannedApplication> scannedApplications){
        for(ScannedApplication scannedApplication : scannedApplications){
            ContentValues values = new ContentValues();
            values.put(ScannedApplicationContract.APPLICATION_NAME, scannedApplication
                    .getInstalledApplication()
                    .getApplicationName());
            values.put(ScannedApplicationContract.PACKAGE_NAME, scannedApplication
                    .getInstalledApplication()
                    .getPackageName());
            values.put(ScannedApplicationContract.APPLICATION_ICON, scannedApplication
                    .getInstalledApplication()
                    .getApplicationIconBytes());
            values.put(ScannedApplicationContract.APPLICATION_RISK_RATE, scannedApplication
                    .getInstalledApplication()
                    .getApplicationRiskRate());
            String[] permissions = scannedApplication.getInstalledApplication().getApplicationPermissions();
            if (permissions == null){
                values.put(ScannedApplicationContract.APPLICATION_PERMISSIONS, TextUtils.join(ARRAY_DIVIDER, new String[]{}));
            } else {
                values.put(ScannedApplicationContract.APPLICATION_PERMISSIONS, TextUtils.join(ARRAY_DIVIDER, permissions));
            }
            values.put(ScannedApplicationContract.SCAN_DATE, scannedApplication.getScanDate());

            mContext.getContentResolver().insert(ScannedApplicationContract.CONTENT_URI, values);
        }
    }
}
