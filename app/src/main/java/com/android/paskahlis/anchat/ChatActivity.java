package com.android.paskahlis.anchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.paskahlis.anchat.adapter.ChatListAdapter;
import com.android.paskahlis.anchat.adapter.MessageRecyclerAdapter;
import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.entity.FirebaseEntityChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private final int ATTACH_FILE_RC = 1998;

    private RecyclerView recyclerViewChatsContainer;
    private EditText editTextMessageInput;
    private Button buttonSendMessage;

    private MessageRecyclerAdapter recyclerAdapter;
    private List<FirebaseEntityChat> chatList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference stReference = storage.getReference();

    private String target;
    private String self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerViewChatsContainer = (RecyclerView) findViewById(R.id.recycler_view_chats_container);
        editTextMessageInput = (EditText) findViewById(R.id.edit_text_message_input);
        buttonSendMessage = (Button) findViewById(R.id.button_send_message);

        firebaseAuth = FirebaseAuth.getInstance();
        target = getIntent().getStringExtra(ChatListAdapter.EXTRA_ID);
        self = firebaseAuth.getCurrentUser().getUid();
        dbReference.child(EntityUser.USER_ROOT).child(target)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getSupportActionBar().setTitle(dataSnapshot.getValue(EntityUser.class).getDisplayName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        recyclerAdapter = new MessageRecyclerAdapter(this, chatList);
        recyclerViewChatsContainer.setAdapter(recyclerAdapter);
        recyclerViewChatsContainer.setLayoutManager(new LinearLayoutManager(this));

        dbReference.child(FirebaseEntityChat.CHAT_ROOT).child(self).child(target)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("ANCHAT", dataSnapshot.getValue().toString());
                        FirebaseEntityChat eChat = dataSnapshot.getValue(FirebaseEntityChat.class);
                        chatList.add(eChat);
                        int newChatPosition = chatList.size() - 1;
                        recyclerAdapter.notifyItemInserted(newChatPosition);
                        recyclerViewChatsContainer.scrollToPosition(newChatPosition);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        editTextMessageInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                recyclerViewChatsContainer.scrollToPosition(chatList.size() - 1);
            }
        });
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }

            private void sendMessage() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                String msg = editTextMessageInput.getText().toString();
                if (msg.isEmpty()) return;
                editTextMessageInput.setText("");
                FirebaseEntityChat chat = new FirebaseEntityChat(FirebaseEntityChat.CONTENT_NONE,
                        msg, self);
                setMessageDb(chat);
//                chatList.add(chat);
//                int newChatPosition = chatList.size() - 1;
//                recyclerAdapter.notifyItemInserted(newChatPosition);
//                recyclerViewChatsContainer.scrollToPosition(newChatPosition);
            }
        });
    }

    private String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd | HH:mm");
        return dateFormat.format(new Date());
    }

    private void setMessageDb(FirebaseEntityChat chat) {
        String ts = getTimeStamp();
        dbReference.child(FirebaseEntityChat.CHAT_ROOT).child(self).child(target)
                .child(ts).setValue(chat);
        dbReference.child(FirebaseEntityChat.CHAT_ROOT).child(target).child(self)
                .child(ts).setValue(chat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_attach:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, ATTACH_FILE_RC);
                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ATTACH_FILE_RC:
                    final Uri file = data.getData();
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("sending...");
                    dialog.setCancelable(false);
                    dialog.show();
                    UploadTask uploadTask = stReference.child(self).child(target).putFile(file);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.cancel();
                            FirebaseEntityChat chat = new FirebaseEntityChat(taskSnapshot
                                    .getDownloadUrl().toString(), getFileName(file), self);
                            String ts = getTimeStamp();
                            dbReference.child(FirebaseEntityChat.CHAT_ROOT).child(self).child(target)
                                    .child(ts).setValue(chat);
                            dbReference.child(FirebaseEntityChat.CHAT_ROOT).child(target).child(self)
                                    .child(ts).setValue(chat);
                            Toast.makeText(getApplicationContext(),
                                    "File sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
