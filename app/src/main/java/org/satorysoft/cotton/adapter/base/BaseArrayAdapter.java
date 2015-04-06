package org.satorysoft.cotton.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by viacheslavokolitiy on 06.04.2015.
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {
    protected final Context context;
    protected final ArrayList<T> items;
    protected LayoutInflater inflater;
    private View inflatedView;

    public BaseArrayAdapter(Context context, int resource, ArrayList<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    protected View inflate(int viewId, ViewGroup container, boolean attachToRoot){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = inflater.inflate(viewId, container, attachToRoot);

        return inflatedView;
    }
}
