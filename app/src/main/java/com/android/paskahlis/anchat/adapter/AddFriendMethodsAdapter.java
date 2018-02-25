package com.android.paskahlis.anchat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.paskahlis.anchat.R;

/**
 * Created by paskahlis on 25/02/2018.
 */

public class AddFriendMethodsAdapter extends BaseAdapter {
    String[] labelsMethod;
    int[] iconsMethod;
    Context context;

    public AddFriendMethodsAdapter(Context context, String[] labels, int[] icon) {
        this.context = context;
        this.labelsMethod = labels;
        this.iconsMethod = icon;
    }

    @Override
    public int getCount() {
        return labelsMethod.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.add_friend_method_item, viewGroup, false);
        ImageView icon = rootView.findViewById(R.id.icon_method);
        TextView label = rootView.findViewById(R.id.label_method);
        icon.setImageDrawable(context.getResources().getDrawable(iconsMethod[i]));
        label.setText(labelsMethod[i]);
        return rootView;
    }
}
