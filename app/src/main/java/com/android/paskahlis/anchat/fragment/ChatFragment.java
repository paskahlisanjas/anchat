package com.android.paskahlis.anchat.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.android.paskahlis.anchat.NewFriend;
import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.adapter.ChatListAdapter;
import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.entity.FirebaseEntityChat;
import com.android.paskahlis.anchat.listener.ClickListener;
import com.android.paskahlis.anchat.listener.RecyclerTouchListener;
import com.android.paskahlis.anchat.model.ChatPreview;
import com.android.paskahlis.anchat.widget.ChatListDivider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private RecyclerView chatListRecyclerView;
    private FloatingActionButton newChat;
    private ChatListAdapter mAdapter;
    private List<ChatPreview> chatList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }


    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        auth = FirebaseAuth.getInstance();
        chatListRecyclerView = rootView.findViewById(R.id.chat_list);
        newChat = rootView.findViewById(R.id.new_chat);

        getChatFromDB();

        mAdapter = new ChatListAdapter(getActivity(), chatList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        chatListRecyclerView.setLayoutManager(mLayoutManager);
        chatListRecyclerView.addItemDecoration(new ChatListDivider(getActivity(), LinearLayoutManager.VERTICAL, 5));
        chatListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatListRecyclerView.setAdapter(mAdapter);
        chatListRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), chatListRecyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Log.d("Recycle Onclick", "item ke : " + position + 1);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewFriend.class));
            }
        });
        return rootView;
    }

    private void getChatFromDB() {
        /*ChatListDBHelper db = new ChatListDBHelper(getActivity());
        ChatPreview chat = new ChatPreview();
        chat.setUserId("13515060@std.stei.itb.ac.id");
        chat.setName("Fajar");
        chat.setProfilePic("");
        chat.setTextChat("Halo");
        chat.setTimestamp("19:08");
        db.addChat(chat);
        chatList = db.getAllChats();*/
        Log.d("ANCHAT", "getChatFromDb(), uid = " + auth.getCurrentUser().getUid());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("fetching message...");
        pDialog.show();

        reference.child(FirebaseEntityChat.CHAT_ROOT).child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pDialog.cancel();
                        if (!dataSnapshot.hasChildren()) {
                            Log.d("ANCHAT", "may have no children");
                            return;
                        }
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            pDialog.show();
                            final ChatPreview preview = new ChatPreview();
                            DataSnapshot lastChat = null;
                            for (DataSnapshot grandChild : child.getChildren()) {
                                Log.d("ANCHAT", "grandChild = " + grandChild.getKey());
                                lastChat = grandChild;
                            }
                            if (lastChat == null) return;
                            FirebaseEntityChat chat = lastChat.getValue(FirebaseEntityChat.class);
                            preview.setUserId(child.getKey());
                            preview.setTimestamp(lastChat.getKey().split(" | ")[2]);
                            preview.setTextChat(chat.getText());
                            preview.setProfilePic("");
                            Log.d("ANCHAT", "retrieving user ...");
                            reference.child(EntityUser.USER_ROOT).child(preview.getUserId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            pDialog.cancel();
                                            EntityUser user = dataSnapshot.getValue(EntityUser.class);
                                            preview.setName(user.getDisplayName());
                                            chatList.add(preview);
                                            int newChatPosition = chatList.size() - 1;
                                            mAdapter.notifyItemInserted(newChatPosition);
                                            preview.setProfilePic(
                                                    user.getProfilePicture() == null ? ""
                                                            : user.getProfilePicture()
                                            );
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pDialog.cancel();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
