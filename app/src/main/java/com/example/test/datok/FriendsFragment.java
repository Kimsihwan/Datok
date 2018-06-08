package com.example.test.datok;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUserDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));


        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends, FriendViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendViewHolder>(

                Friends.class,
                R.layout.users_single_layout,
                FriendViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendViewHolder friendViewHolder, Friends friends, int i) {
                friendViewHolder.setDate(friends.getDate());

                String list_user_id = getRef(i).getKey();

                mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")) {
                            Boolean userOnline = (Boolean) dataSnapshot.child("online").getValue();
                            friendViewHolder.setUserOnline(userOnline);
                        }

                        friendViewHolder.setName(userName);
                        friendViewHolder.setUserImage(userThumb, getContext());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mFriendsList.setAdapter(friendsRecyclerViewAdapter);
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDate(String date) {

            TextView userStatusView = mView.findViewById(R.id.user_single_status);
            userStatusView.setText(date);

        }

        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = mView.findViewById(R.id.user_single_image);
            Picasso.get().load(thumb_image).placeholder(R.drawable.ic_action_name).into(userImageView);

        }

        public void setUserOnline(boolean online_status) {

            ImageView userOnlineView = mView.findViewById(R.id.user_single_online_icon);

            if(online_status == true) {

                userOnlineView.setVisibility(View.VISIBLE);

            } else {
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }


    }

}
