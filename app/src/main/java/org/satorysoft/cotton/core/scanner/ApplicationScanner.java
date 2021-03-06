package org.satorysoft.cotton.core.scanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.CompletedScanEvent;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.DaggerCoreComponent;
import org.satorysoft.cotton.di.component.DaggerRootComponent;
import org.satorysoft.cotton.di.component.RootComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.util.ApplicationRiskUtil;
import org.satorysoft.cotton.util.BooleanPreference;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DrawableConverter;

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
        this.mCoreComponent = DaggerCoreComponent.builder().coreModule(new CoreModule(mContext)).build();
        this.rootComponent = DaggerRootComponent.builder().rootModule(new RootModule(mContext)).build();
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
                byte[] imageRepresentation = new DrawableConverter().convertDrawable(applicationIcon);

                try {
                    PackageInfo permissionsInfo = mPackageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
                    String[] applicationPermissions = permissionsInfo.requestedPermissions;
                    double applicationRiskRate = (double)new ApplicationRiskUtil(getDangerousPermissions())
                            .evaluateApplicationRisk(applicationPermissions);
                    installedApplication.setApplicationName(applicationName);
                    installedApplication.setPackageName(packageName);
                    installedApplication.setApplicationIconBytes(imageRepresentation);
                    installedApplication.setApplicationRiskRate(applicationRiskRate);
                    installedApplication.setApplicationPermissions(applicationPermissions);
                    scannedApplication.setInstalledApplication(installedApplication);
                    scannedApplication.setScanDate(System.currentTimeMillis());
                    scannedApplications.add(scannedApplication);

                    publishProgress(progress++);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return scannedApplications;
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
        mProgress.setProgress(Constants.SCAN_COMPLETED);
        EventBus.getDefault().post(new CompletedScanEvent());
        BooleanPreference preference = rootComponent.getBooleanPreference();
        preference.set(mContext.getString(R.string.text_first_run), false);
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
