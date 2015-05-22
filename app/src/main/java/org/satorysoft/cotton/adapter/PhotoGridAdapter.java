package org.satorysoft.cotton.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public Object getItem(int i) {
        return imageURLs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(Constants.PHOTO_GRID_WIDTH,
                    Constants.PHOTO_GRID_HEIGHT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(Constants.IMAGE_VIEW_PADDING_LEFT,
                    Constants.IMAGE_VIEW_PADDING_TOP,
                    Constants.IMAGE_VIEW_PADDING_RIGHT,
                    Constants.IMAGE_VIEW_PADDING_BOTTOM);
        } else {
            imageView = (ImageView) convertView;
        }

        /*Bitmap bm = decodeSampledBitmapFromUri(imageURLs.get(position), Constants.REQUIRED_WIDTH,
                Constants.REQUIRED_HEIGHT);*/

        Uri uri = Uri.fromFile(new File(imageURLs.get(position)));

        Picasso.with(context).load(uri).resize(96,96).centerCrop().into(imageView);
        return imageView;
    }

    /*public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }*/
}
