package com.android.paskahlis.anchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.entity.EntityChat;
import com.android.paskahlis.anchat.entity.EntityChatContent;

import org.w3c.dom.Text;

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
        if (content.getMessageContentType() == EntityChatContent.MESSAGE_CONTENT_TYPE_TEXT) {
            holder.fileNameIn.setVisibility(View.GONE);
            holder.filenameOut.setVisibility(View.GONE);
            if (chat.getMessageDirection() == EntityChat.MESSAGE_DIRECTION_IN) {
                holder.textIn.setText((String) content.getMessage());
                holder.textOut.setVisibility(View.GONE);
            } else {
                holder.textOut.setText((String) content.getMessage());
                holder.textIn.setVisibility(View.GONE);
            }
        } else {
            holder.textOut.setVisibility(View.GONE);
            holder.textIn.setVisibility(View.GONE);
            if (chat.getMessageDirection() == EntityChat.MESSAGE_DIRECTION_IN) {
                holder.filenameOut.setVisibility(View.GONE);
                holder.fileNameIn.setText((String) content.getMessage());
            } else {
                holder.fileNameIn.setVisibility(View.GONE);
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

        public ViewHolder(View view) {
            super(view);
            filenameOut = (TextView) view.findViewById(R.id.file_name_out);
            fileNameIn = (TextView) view.findViewById(R.id.file_name_in);
            textIn = (TextView) view.findViewById(R.id.message_text_in);
            textOut = (TextView) view.findViewById(R.id.message_text_out);
        }
    }


}
