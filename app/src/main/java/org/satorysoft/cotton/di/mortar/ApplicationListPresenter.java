package org.satorysoft.cotton.di.mortar;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.satorysoft.cotton.adapter.ApplicationListAdapter;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.DaggerCoreComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.ui.animator.SlideInFromLeftItemAnimator;
import org.satorysoft.cotton.ui.view.ApplicationListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
@Singleton
public class ApplicationListPresenter extends ViewPresenter<ApplicationListView> {

    private CoreComponent mCoreComponent;
    private boolean needSortByRisk;
    private boolean needSortByName;

    @Inject
    public ApplicationListPresenter() {

    }

    public void populateListView(RecyclerView recyclerView, Context context) {
        this.mCoreComponent = DaggerCoreComponent.builder().coreModule(new CoreModule(context)).build();
        Cursor cursor = context.getContentResolver().query(ScannedApplicationContract.CONTENT_URI,
                null, null, null, null);
        List<ScannedApplication> scannedApplicationList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                String applicationName = cursor.getString(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_NAME));
                byte[] value = cursor.getBlob(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_ICON));
                double risk = cursor.getDouble(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_RISK_RATE));
                ScannedApplication scannedApplication = new ScannedApplication();
                InstalledApplication installedApplication = new InstalledApplication();
                installedApplication.setApplicationName(applicationName);
                installedApplication.setApplicationIconBytes(value);
                installedApplication.setApplicationRiskRate(risk);
                scannedApplication.setInstalledApplication(installedApplication);
                scannedApplicationList.add(scannedApplication);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ApplicationListAdapter adapter = mCoreComponent.getAdapter();

        if (needSortByRisk) {
            Collections.sort(scannedApplicationList, new Comparator<ScannedApplication>() {
                @Override
                public int compare(ScannedApplication firstApplication,
                                   ScannedApplication secondApplication) {
                    Double firstApplicationRisk = firstApplication.getInstalledApplication().getApplicationRiskRate();
                    Double secondApplicationRisk = secondApplication.getInstalledApplication().getApplicationRiskRate();
                    return firstApplicationRisk.compareTo(secondApplicationRisk);
                }
            });
        }

        if (needSortByName) {
            Collections.sort(scannedApplicationList, new Comparator<ScannedApplication>() {
                @Override
                public int compare(ScannedApplication firstApplication, ScannedApplication secondApplication) {
                    return secondApplication
                            .getInstalledApplication()
                            .getApplicationName()
                            .compareTo(
                                    firstApplication
                                            .getInstalledApplication()
                                            .getApplicationName()
                            );
                }
            });
        }

        for (ScannedApplication scannedApplication : scannedApplicationList) {
            adapter.addItem(scannedApplication);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInFromLeftItemAnimator(recyclerView));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void sortListByRisk(RecyclerView recyclerView, Context context) {
        needSortByRisk = true;
        needSortByName = false;
        populateListView(recyclerView, context);
    }

    public void sortListByApplicationName(RecyclerView recyclerView, Context context) {
        needSortByRisk = false;
        needSortByName = true;
        populateListView(recyclerView, context);
    }
}
