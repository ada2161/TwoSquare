package com.example.anuraag.twosquare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {


    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Context context;

        public TextView messageTextView;
        public TextView messengerTextView;
        public ImageView messengerImageView;
        public RestaurantViewHolder(View v) {
            super(v);
            context = itemView.getContext();
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(MainActivity.this,RestaurantReviews.class).putExtra("RestaurantName", messageTextView.getText() );;
                    startActivity(i);
                    Log.d("RecyclerView", "onClick：" + messageTextView.getText());
                }
            });*/
            messengerImageView = (ImageView) itemView.findViewById(R.id.messengerImageView);
           // v.setOnClickListener(new MyOnClickListener(this,messageTextView.getText()));
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d("sa",String.valueOf(getAdapterPosition()));
            Log.d("positon-of-clicked-item", String.valueOf(messageTextView.getText()));
            Intent i = new Intent(context , RestaurantReviews.class );
            i.putExtra("RestaurantName",String.valueOf(messageTextView.getText()));
            context.startActivity(i);
        }

    }



    //---Remove Later
    public static final String MESSAGES_CHILD = "Restaurants";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;

    private RecyclerView mMessageRecyclerView;
    private String mPhotoUrl;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRestaurantRecyclerView;

    public static final String ANONYMOUS = "anonymous";
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, RestaurantViewHolder>
            mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRestaurantRecyclerView= (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        //mLinearLayoutManager.setStackFromEnd(true);
        mRestaurantRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        Button btn=(Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),addRestaurant.class));
            }
        });
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage,
                RestaurantViewHolder>(
                FriendlyMessage.class,
                R.layout.restaurant_list,
                RestaurantViewHolder.class,
                mFirebaseDatabaseReference.child("Restaurants")) {

            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder,
                                              FriendlyMessage friendlyMessage, int position) {
                 //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                 viewHolder.messageTextView.setText(friendlyMessage.getName());

                 if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(MainActivity.this,
                                            R.mipmap.pholder));
                } else {
                    Glide.with(MainActivity.this)
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mRestaurantRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mRestaurantRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRestaurantRecyclerView.setAdapter(mFirebaseAdapter);
    }

    public class MyOnClickListener implements View.OnClickListener
    {

        String RestaurnatName;
        public MyOnClickListener(String Restaurantname) {
            this.RestaurnatName = Restaurantname;
        }

        @Override
        public void onClick(View v)
        {

        }
    };
}