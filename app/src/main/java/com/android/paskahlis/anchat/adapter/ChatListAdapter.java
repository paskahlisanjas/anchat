package com.android.paskahlis.anchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.model.ChatPreview;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fajar on 22/02/18.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<ChatPreview> mChat = null;
    private Context mContext;

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderName;
        private TextView chatMessage;
        private TextView timestamp;
        private CircleImageView senderProfilePic;

        public ChatViewHolder(View itemView) {
            super(itemView);

            senderName          = itemView.findViewById(R.id.name);
            chatMessage         = itemView.findViewById(R.id.chat_message);
            timestamp           = itemView.findViewById(R.id.timestamp);
            senderProfilePic    = itemView.findViewById(R.id.profile_picture);
        }
    }

    public ChatListAdapter (List<ChatPreview> chatPreviews, Context  context){
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
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        ChatPreview chat = mChat.get(position);

        holder.senderName.setText(chat.getName());
        holder.chatMessage.setText(chat.getTextChat());
        holder.timestamp.setText(chat.getTimestamp());
        Glide.with(mContext).load(chat.getProfilePic()).into(holder.senderProfilePic);
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
}