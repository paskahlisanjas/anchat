package com.android.paskahlis.anchat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.entity.FirebaseEntityChat;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder> {
    private List<FirebaseEntityChat> chatList;
    private Context context;
    private ProgressDialog pDialog;

    public MessageRecyclerAdapter(Context context, List<FirebaseEntityChat> chatList) {
        this.context = context;
        this.chatList = chatList;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading...");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseEntityChat chat = chatList.get(position);
        if (chat.getSender().equals(auth.getCurrentUser().getUid())) {
            holder.containerFileIn.setVisibility(GONE);
            holder.containerTextIn.setVisibility(GONE);
            if (chat.getContent().equals("-")) {
                holder.containerFileOut.setVisibility(GONE);
                holder.textOut.setText(chat.getText());
            } else {
                holder.containerFileOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pDialog.show();
                        new DownloadFileFromUrl().execute(chat.getContent(), chat.getText());
                    }
                });
                holder.containerTextOut.setVisibility(GONE);
                holder.filenameOut.setText(chat.getText());
            }
        } else {
            holder.containerTextOut.setVisibility(GONE);
            holder.containerFileOut.setVisibility(GONE);
            if (chat.getContent().equals(FirebaseEntityChat.CONTENT_NONE)) {
                holder.containerFileIn.setVisibility(GONE);
                holder.textIn.setText(chat.getText());
            } else {
                holder.containerFileIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DownloadFileFromUrl().execute(chat.getContent(), chat.getText());
                    }
                });
                holder.containerTextIn.setVisibility(GONE);
                holder.fileNameIn.setText(chat.getText());
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

    class DownloadFileFromUrl extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream iStream = new BufferedInputStream(url.openStream(), 8129);
                String path = Environment.getExternalStorageDirectory().toString() + "/" + strings[1];
                OutputStream oStream = new FileOutputStream(path);
                byte[] data = new byte[1024];
                int count;
                while ((count = iStream.read(data)) != -1) {
                    oStream.write(data, 0, count);
                }
                oStream.flush();
                oStream.close();
                iStream.close();
                pDialog.cancel();
                File file = new File(path);
                String mime = context.getContentResolver().getType(Uri.fromFile(file));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), mime);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
