package com.android.paskahlis.anchat.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by fajar on 22/02/18.
 */

public class MessagingInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase Token", "Refreshed token: " + refreshedToken);
    }
}
