package com.android.paskahlis.anchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.paskahlis.anchat.model.ChatPreview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fajar on 22/02/18.
 */

public class ChatListDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chatListManager";
    private static final String TABLE_CHATS = "chats";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_PIC = "profile_picture";
    private static final String KEY_CHAT = "last_chat";
    private static final String KEY_TIMESTAMP = "timestamp";

    public ChatListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHATS_TABLE = "CREATE TABLE " + TABLE_CHATS + "(" +
                KEY_EMAIL + " VARCHAR(32) PRIMARY KEY, " + KEY_NAME +
                "VARCHAR(32), " + KEY_PIC + " TEXT," + KEY_CHAT +
                " TEXT, " + KEY_TIMESTAMP + " VARCHAR(64))";
        db.execSQL(CREATE_CHATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);

        onCreate(db);
    }

    public void addChat(ChatPreview chat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, chat.getEmail());
        values.put(KEY_NAME, chat.getName());
        values.put(KEY_PIC, chat.getProfilePic());
        values.put(KEY_CHAT, chat.getTextChat());
        values.put(KEY_TIMESTAMP, chat.getTimestamp());

        db.insertWithOnConflict(TABLE_CHATS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void removeChat(String email) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_CHATS, KEY_EMAIL + "= ?" ,new String[] { email });
        db.close();
    }

    public List<ChatPreview> getAllChats (){
        List<ChatPreview> chatList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHATS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatPreview chat = new ChatPreview();
                chat.setEmail(cursor.getString(0));
                chat.setName(cursor.getString(1));
                chat.setProfilePic(cursor.getString(2));
                chat.setTextChat(cursor.getString(3));
                chat.setTimestamp(cursor.getString(4));
                // Adding contact to list
                chatList.add(chat);
            } while (cursor.moveToNext());
        }

        // return contact list
        return chatList;

    }


}
