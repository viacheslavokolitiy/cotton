package org.satorysoft.cotton.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.core.model.SelectedApplication;
import org.satorysoft.cotton.core.scanner.ApplicationScanner;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.ui.activity.ApplicationDetailActivity;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.IDrawableStateManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.AppListViewHolder>
        implements IDrawableStateManager {
    private List<ScannedApplication> scannedApplications = new ArrayList<>();
    private Context context;

    @Inject
    public ApplicationListAdapter(Context context){
        this.context = context;
    }

    @Override
    public AppListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_application_item, parent, false);
        return new AppListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppListViewHolder holder, int position) {
        holder.applicationLogo.setImageDrawable(restoreDrawable(scannedApplications
                .get(position)
                .getInstalledApplication()
                .getApplicationIconBytes()));
        holder.applicationTitle.setText(scannedApplications
                .get(position)
                .getInstalledApplication()
                .getApplicationName());
        double risk = scannedApplications.get(position).getInstalledApplication().getApplicationRiskRate();
        if(risk > 0.5){
            holder.riskIndicator.setBackgroundColor(context.getResources().getColor(R.color.md_red_500));
        }

        if(risk == 0.5){
            holder.riskIndicator.setBackgroundColor(context.getResources().getColor(R.color.md_yellow_500));
        }

        if(risk < 0.5){
            holder.riskIndicator.setBackgroundColor(context.getResources().getColor(R.color.md_light_green_500));
        }
    }

    @Override
    public int getItemCount() {
        return scannedApplications.size();
    }

    public void addItem(ScannedApplication application){
        scannedApplications.add(0, application);
        notifyItemInserted(0);
    }

    public void removeItem(){
        scannedApplications.remove(0);
        notifyItemRemoved(0);
    }

    class AppListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @FindView(R.id.application_icon)
        protected ImageView applicationLogo;
        @FindView(R.id.text_application_name)
        protected RobotoTextView applicationTitle;
        @FindView(R.id.risk_indicator)
        protected LinearLayout riskIndicator;

        public AppListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Drawable drawable = applicationLogo.getDrawable();
            String title = applicationTitle.getText().toString();
            String[] permissions;

            Cursor cursor = context.getContentResolver().query(ScannedApplicationContract.CONTENT_URI,
                    null, ScannedApplicationContract.APPLICATION_NAME + "=?",
                    new String[]{title}, null);
            if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
                permissions = cursor.getString(cursor.getColumnIndex(ScannedApplicationContract
                        .APPLICATION_PERMISSIONS))
                        .split(ApplicationScanner.ARRAY_DIVIDER.toString());
            } else {
                permissions = new String[]{};
            }

            cursor.close();

            SelectedApplication selectedApplication = new SelectedApplication();
            selectedApplication.setIcon(convertToBytes(drawable));
            selectedApplication.setTitle(title);
            selectedApplication.setPermissions(permissions);

            Intent intent = new Intent(context, ApplicationDetailActivity.class);
            intent.putExtra(Constants.SCANNED_APPLICATION, selectedApplication);
            context.startActivity(intent);
        }
    }

    @Override public byte[] convertToBytes(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @SuppressWarnings("deprecation")
    @Override public Drawable restoreDrawable(byte[] bytes){
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
}
