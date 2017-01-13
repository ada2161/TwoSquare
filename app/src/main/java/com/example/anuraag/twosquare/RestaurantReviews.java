package com.example.anuraag.twosquare;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.anuraag.twosquare.MainActivity.MESSAGES_CHILD;

/**
 * Created by anuraag on 1/6/2017.
 */

public class RestaurantReviews extends AppCompatActivity{


    public static class RestaurantReviewsViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        public TextView messageTextView;
        public TextView messengerTextView;
        public ImageView messengerImageView;
        public RestaurantReviewsViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            /*v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(MainActivity.this,RestaurantReviews.class).putExtra("RestaurantName", messageTextView.getText() );;
                    startActivity(i);
                    Log.d("RecyclerView", "onClick：" + messageTextView.getText());
                }
            });*/
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (ImageView) itemView.findViewById(R.id.messengerImageView);
        }

    }
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private String mUsername = mFirebaseUser.getDisplayName();
    private Button mSendButton;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, RestaurantReviewsViewHolder>
            mFirebaseAdapter;
    private RecyclerView mMessageRecyclerView;
    private String mPhotoUrl;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRestaurantRecyclerView;
    private EditText mMessageEditText;
    private Button msendButton;
    TextView restaurantName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        mRestaurantRecyclerView= (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRestaurantRecyclerView.setLayoutManager(mLinearLayoutManager);

        final String s= getIntent().getStringExtra("RestaurantName");

        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new
                        FriendlyMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl);
                mFirebaseDatabaseReference.child(s)
                        .push().setValue(friendlyMessage);
                mMessageEditText.setText("");
                //sendNotificationToUser(friendlyMessage.getName(), friendlyMessage.getText());
            }
        });

        restaurantName = (TextView)findViewById(R.id.restaurant_name);
        restaurantName.setText(s);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage,
                RestaurantReviewsViewHolder>(
                FriendlyMessage.class,
                R.layout.single_review,
                RestaurantReviewsViewHolder.class,
                mFirebaseDatabaseReference.child(s)) {

            @Override
            protected void populateViewHolder(RestaurantReviewsViewHolder viewHolder,
                                              FriendlyMessage friendlyMessage, int position) {
                viewHolder.messageTextView.setText(friendlyMessage.getText());
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(RestaurantReviews.this,
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(RestaurantReviews.this)
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
        mMessageEditText = (EditText) findViewById(R.id.messageText);
        msendButton = (Button) findViewById(R.id.sendButton);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    msendButton.setEnabled(true);
                } else {
                    msendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}
