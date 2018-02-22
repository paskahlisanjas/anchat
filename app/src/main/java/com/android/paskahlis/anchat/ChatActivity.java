package com.android.paskahlis.anchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.paskahlis.anchat.adapter.MessageRecyclerAdapter;
import com.android.paskahlis.anchat.entity.EntityChat;
import com.android.paskahlis.anchat.entity.EntityChatContent;
import com.android.paskahlis.anchat.entity.EntityChatContentText;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerViewChatsContainer;
    EditText editTextMessageInput;
    Button buttonSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerViewChatsContainer = (RecyclerView)findViewById(R.id.recycler_view_chats_container);
        editTextMessageInput = (EditText)findViewById(R.id.edit_text_message_input);
        buttonSendMessage = (Button)findViewById(R.id.button_send_message);

        List<EntityChat> chatList = new ArrayList<>();
        EntityChatContent content = new EntityChatContentText("This is the content");
        EntityChat chat = new EntityChat("Sender", EntityChat.MESSAGE_DIRECTION_OUT, content);
        chatList.add(chat);
        recyclerViewChatsContainer.setAdapter(new MessageRecyclerAdapter(chatList));
        recyclerViewChatsContainer.setLayoutManager(new LinearLayoutManager(this));

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }

            private void sendMessage() {
                
            }
        });
    }


}
