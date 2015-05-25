package org.satorysoft.cotton.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.satorysoft.cotton.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 18.05.2015.
 */
public class PhotoGridAdapter extends BaseAdapter {
    private final Context context;
    private List<String> imageURLs = new ArrayList<>();
    private ImageView imageView;

    public PhotoGridAdapter(Context context){
        this.context = context;
    }

    public void addImage(String imageURL){
        imageURLs.add(imageURL);
    }

    @Override
    public int getCount() {
        return imageURLs.size();
    }

    @Override
    public Object getItem(int position) {
        return imageURLs.get(position);
    }

    @Override
    public long getItemId(int itemID) {
        return itemID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(Constants.PHOTO_GRID_WIDTH,
                Constants.PHOTO_GRID_HEIGHT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(Constants.IMAGE_VIEW_PADDING_LEFT,
                Constants.IMAGE_VIEW_PADDING_TOP,
                Constants.IMAGE_VIEW_PADDING_RIGHT,
                Constants.IMAGE_VIEW_PADDING_BOTTOM);

        Uri uri = Uri.fromFile(new File(imageURLs.get(position)));

        Picasso.with(context)
                .load(uri)
                .resize(Constants.IMAGE_NEW_WIDTH, Constants.IMAGE_NEW_HEIGHT)
                .centerCrop()
                .into(imageView);
        return imageView;
    }
}
