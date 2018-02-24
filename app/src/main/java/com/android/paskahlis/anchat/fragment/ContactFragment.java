package com.android.paskahlis.anchat.fragment;

import android.content.Context;
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

import com.android.paskahlis.anchat.R;
import com.android.paskahlis.anchat.adapter.ContactListAdapter;
import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.listener.ClickListener;
import com.android.paskahlis.anchat.listener.RecyclerTouchListener;
import com.android.paskahlis.anchat.widget.ChatListDivider;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView contactRecyclerView;
    private ContactListAdapter mAdapter;
    private List<EntityUser> contactList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addFriend;

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
        addFriend           = rootView.findViewById(R.id.add_friend);

        addContactDummy();
        mAdapter    = new ContactListAdapter(getActivity(), contactList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.addItemDecoration(new ChatListDivider(getActivity(), LinearLayoutManager.VERTICAL, 5));
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.setAdapter(mAdapter);

        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), contactRecyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Log.d("Recycle Onclick", "item ke : " + position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void addContactDummy(){
        contactList = new ArrayList<>();
        EntityUser fajar = new EntityUser("Fajar Nugroho", null, 7.937193, 8.782781, "", "Sibuk");
        EntityUser bethea = new EntityUser("Bethea", null, 7.937193, 8.782781, "", "Gabut");
        contactList.add(fajar);
        contactList.add(bethea);
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
