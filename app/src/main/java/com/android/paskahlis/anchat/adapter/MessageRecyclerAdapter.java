package com.android.paskahlis.anchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.entity.EntityChat;
import com.android.paskahlis.anchat.entity.EntityChatContent;

import java.util.List;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {
    private List<EntityChat> chatList;

    public MessageRecyclerAdapter(List<EntityChat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EntityChat chat = chatList.get(position);
        EntityChatContent content = chat.getContent();
        if (content.getMessageType() == EntityChatContent.MESSAGE_CONTENT_TYPE_TEXT) {
            holder.containerFileIn.setVisibility(View.GONE);
            holder.containerFileOut.setVisibility(View.GONE);
            if (chat.getMessageDirection() == EntityChat.MESSAGE_DIRECTION_IN) {
                holder.textIn.setText((String) content.getMessage());
                holder.containerTextOut.setVisibility(View.GONE);
            } else {
                holder.textOut.setText((String) content.getMessage());
                holder.containerTextIn.setVisibility(View.GONE);
            }
        } else {
            holder.containerTextOut.setVisibility(View.GONE);
            holder.containerTextIn.setVisibility(View.GONE);
            if (chat.getMessageDirection() == EntityChat.MESSAGE_DIRECTION_IN) {
                holder.containerFileOut.setVisibility(View.GONE);
                holder.fileNameIn.setText((String) content.getMessage());
            } else {
                holder.containerFileIn.setVisibility(View.GONE);
                holder.filenameOut.setText((String) content.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameIn, filenameOut, textIn, textOut;
        LinearLayout containerFileIn, containerFileOut, containerTextIn, containerTextOut;

        public ViewHolder(View view) {
            super(view);
            filenameOut = (TextView) view.findViewById(R.id.file_name_out);
            fileNameIn = (TextView) view.findViewById(R.id.file_name_in);
            textIn = (TextView) view.findViewById(R.id.message_text_in);
            textOut = (TextView) view.findViewById(R.id.message_text_out);

            containerFileIn = (LinearLayout) view.findViewById(R.id.container_message_in_file);
            containerFileOut = (LinearLayout) view.findViewById(R.id.container_message_out_file);
            containerTextIn = (LinearLayout) view.findViewById(R.id.container_message_in_text);
            containerTextOut = (LinearLayout) view.findViewById(R.id.container_message_out_text);
        }
    }


}
