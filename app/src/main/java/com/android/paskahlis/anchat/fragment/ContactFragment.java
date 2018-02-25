package com.android.paskahlis.anchat.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.paskahlis.anchat.ChatActivity;
import com.android.paskahlis.anchat.NewFriend;
import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.adapter.ContactListAdapter;
import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.listener.ClickListener;
import com.android.paskahlis.anchat.listener.RecyclerTouchListener;
import com.android.paskahlis.anchat.widget.ChatListDivider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = db.getReference();

    private OnFragmentInteractionListener mListener;

    private RecyclerView contactRecyclerView;
    private ContactListAdapter mAdapter;
    private List<EntityUser> contactList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addFriend;
    private List<String> keyList = new ArrayList<>();

    public ContactFragment() {
        // Required empty public constructor
    }


    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        contactRecyclerView = rootView.findViewById(R.id.contact_list);
        addFriend = rootView.findViewById(R.id.add_friend);
        auth = FirebaseAuth.getInstance();
        String selfId = auth.getCurrentUser().getUid();

        getAllContact(selfId);
        mAdapter = new ContactListAdapter(getActivity(), contactList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.addItemDecoration(new ChatListDivider(getActivity(), LinearLayoutManager.VERTICAL, 5));
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.setAdapter(mAdapter);

        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), contactRecyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(ChatActivity.EXTRA_ID, keyList.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewFriend.class));
            }
        });
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getAllContact(String selfId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching...");
        dialog.show();
        Log.d("ANCHAT", "addValuesEventListener...");
        dbReference.child(EntityUser.USER_ROOT).child(selfId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        EntityUser user = dataSnapshot.getValue(EntityUser.class);
                        List<String> contacts = user.getContactList();
                        if (contacts == null) return;
                        for (String contact : contacts) {
                            dialog.show();
                            dbReference.child(EntityUser.USER_ROOT).child(contact)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dialog.dismiss();
                                            EntityUser friend = dataSnapshot.getValue(EntityUser.class);
                                            contactList.add(friend);
                                            keyList.add(dataSnapshot.getKey());
                                            mAdapter.notifyItemInserted(contactList.size() - 1);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), "Something went wrong.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
