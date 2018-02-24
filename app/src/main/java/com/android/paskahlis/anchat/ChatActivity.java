package com.android.paskahlis.anchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.paskahlis.anchat.adapter.ChatListAdapter;
import com.android.paskahlis.anchat.adapter.MessageRecyclerAdapter;
import com.android.paskahlis.anchat.entity.EntityChat;
import com.android.paskahlis.anchat.entity.EntityChatContent;
import com.android.paskahlis.anchat.entity.EntityChatContentText;
import com.android.paskahlis.anchat.entity.EntityUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChatsContainer;
    private EditText editTextMessageInput;
    private Button buttonSendMessage;

    private MessageRecyclerAdapter recyclerAdapter;
    private List<EntityChat> chatList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child(EntityChat.CHAT_ROOT);

    private String target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerViewChatsContainer = (RecyclerView) findViewById(R.id.recycler_view_chats_container);
        editTextMessageInput = (EditText) findViewById(R.id.edit_text_message_input);
        buttonSendMessage = (Button) findViewById(R.id.button_send_message);

        target = getIntent().getStringExtra(ChatListAdapter.EXTRA_ID);
        firebaseAuth = FirebaseAuth.getInstance();
        reference.child(EntityUser.USER_ROOT).child(target)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getSupportActionBar().setTitle(dataSnapshot.getValue(EntityUser.class).getDisplayName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        reference = reference.child(firebaseAuth.getCurrentUser().getUid()).child(target);
        reference.child(firebaseAuth.getCurrentUser().getUid()).child(target)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        recyclerAdapter = new MessageRecyclerAdapter(chatList);
        recyclerViewChatsContainer.setAdapter(recyclerAdapter);
        recyclerViewChatsContainer.setLayoutManager(new LinearLayoutManager(this));

        reference.orderByKey().limitToLast(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
/*                        EntityChatContent cont = new EntityChatContentText((String)dataSnapshot.getValue());
                        EntityChat ch = new EntityChat(target, EntityChat.MESSAGE_DIRECTION_IN, cont);
                        chatList.add(ch);
                        int newChatPosition = chatList.size() - 1;
                        recyclerAdapter.notifyItemInserted(newChatPosition);
                        recyclerViewChatsContainer.scrollToPosition(newChatPosition);*/
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
                EntityChatContent chatContent = new EntityChatContentText(msg);
                EntityChat entityChat = new EntityChat(firebaseAuth.getCurrentUser().getUid(),
                        EntityChat.MESSAGE_DIRECTION_OUT, chatContent);
                chatList.add(entityChat);
                setMessageDb(entityChat);
                int newChatPosition = chatList.size() - 1;
                recyclerAdapter.notifyItemInserted(newChatPosition);
                recyclerViewChatsContainer.scrollToPosition(newChatPosition);
            }
        });
    }

    private void setMessageDb(EntityChat chat) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }
}
