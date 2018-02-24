package com.android.paskahlis.anchat.service;

import android.util.Log;

import com.android.paskahlis.anchat.database.ChatListDBHelper;
import com.android.paskahlis.anchat.model.ChatPreview;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by fajar on 22/02/18.
 */

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        ChatListDBHelper db = new ChatListDBHelper(getApplicationContext());

        ChatPreview chat = new ChatPreview();
        chat.setUserId(remoteMessage.getFrom());
        chat.setName("fajar");
        chat.setTextChat(remoteMessage.getNotification().getBody());
        chat.setTimestamp(String.valueOf(remoteMessage.getSentTime()));
        chat.setProfilePic("");
        db.addChat(chat);
        Log.d("Firebase", "Pesan Masuk");
    }
}
