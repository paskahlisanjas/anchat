package com.android.paskahlis.anchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.paskahlis.anchat.ChatActivity;
import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.model.ChatPreview;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fajar on 22/02/18.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    public static String EXTRA_ID = "user_id";

    private List<ChatPreview> mChat = null;
    private Context mContext;


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderName;
        private TextView chatMessage;
        private TextView timestamp;
        private CircleImageView senderProfilePic;
        private RelativeLayout container;

        public ChatViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container_chat_list);
            senderName = itemView.findViewById(R.id.name);
            chatMessage = itemView.findViewById(R.id.chat_message);
            timestamp = itemView.findViewById(R.id.timestamp);
            senderProfilePic = itemView.findViewById(R.id.profile_picture);
        }
    }

    public ChatListAdapter(Context context, List<ChatPreview> chatPreviews) {
        this.mChat = chatPreviews;
        this.mContext = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_row, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, final int position) {

        final ChatPreview chat = mChat.get(position);

        holder.senderName.setText(chat.getName());
        holder.chatMessage.setText(chat.getTextChat());
        holder.timestamp.setText(chat.getTimestamp());
        if (chat.getProfilePic().equals("")) {
            holder.senderProfilePic.setBackgroundResource(R.drawable.default_profile_pic);
        } else {
            Glide.with(mContext).load(chat.getProfilePic()).into(holder.senderProfilePic);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(EXTRA_ID, chat.getUserId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
}