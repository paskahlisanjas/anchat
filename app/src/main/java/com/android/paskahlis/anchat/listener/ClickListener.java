package com.android.paskahlis.anchat.listener;

import android.view.View;

/**
 * Created by fajar on 23/02/18.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
