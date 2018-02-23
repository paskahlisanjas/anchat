package com.android.paskahlis.anchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fajar on 23/02/18.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.UserViewHolder>{

    private List<EntityUser> muser = null;
    private Context mContext;

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView displayName;
        private TextView userStatus;
        private CircleImageView profilePic;

        public UserViewHolder(View itemView) {
            super(itemView);

            displayName   = itemView.findViewById(R.id.name);
            profilePic    = itemView.findViewById(R.id.profile_picture);
        }
    }

    public ContactListAdapter (Context  context, List<EntityUser> userPreviews){
        this.muser = userPreviews;
        this.mContext = context;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        EntityUser user = muser.get(position);

        holder.displayName.setText(user.getDisplayName());
        if (user.getProfilePicture().equalsIgnoreCase("")){
            holder.profilePic.setBackgroundResource(R.drawable.default_profile_pic);
        } else {
            Glide.with(mContext).load(user.getProfilePicture()).into(holder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return muser.size();
    }
}
