package com.android.paskahlis.anchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
