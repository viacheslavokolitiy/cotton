package org.satorysoft.cotton.adapter;

import android.content.Context;
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
import org.satorysoft.cotton.ui.view.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.AppListViewHolder>{
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

    static class AppListViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.application_icon)
        protected ImageView applicationLogo;
        @InjectView(R.id.text_application_name)
        protected RobotoTextView applicationTitle;
        @InjectView(R.id.risk_indicator)
        protected LinearLayout riskIndicator;

        public AppListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @SuppressWarnings("deprecation")
    private Drawable restoreDrawable(byte[] bytes){
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
}
