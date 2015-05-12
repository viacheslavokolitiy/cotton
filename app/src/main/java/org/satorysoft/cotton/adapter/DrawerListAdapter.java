package org.satorysoft.cotton.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.adapter.base.BaseArrayAdapter;
import org.satorysoft.cotton.core.model.DrawerItem;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.FindView;

/**
 * Created by viacheslavokolitiy on 06.04.2015.
 */
public class DrawerListAdapter extends BaseArrayAdapter<DrawerItem> {

    public DrawerListAdapter(Context context, ArrayList<DrawerItem> objects) {
        super(context, R.layout.drawer_list_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        ViewHolder viewHolder;
        rowView = inflate(R.layout.drawer_list_item, parent, false);
        viewHolder = new ViewHolder(rowView);
        rowView.setTag(viewHolder);

        DrawerItem item = getItem(position);
        viewHolder.drawerItemTitle.setText(item.getDrawerItemTitle());

        return rowView;
    }

    static class ViewHolder {
        @FindView(R.id.text_drawer_item)
        protected RobotoTextView drawerItemTitle;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
