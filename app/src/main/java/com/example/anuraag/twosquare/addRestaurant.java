package com.example.anuraag.twosquare;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.anuraag.twosquare.R.id.Raddress;
import static com.example.anuraag.twosquare.R.id.Rname;

/**
 * Created by Ankita on 1/13/2017.
 */

public class addRestaurant extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private String mUsername = mFirebaseUser.getDisplayName();
    private Button mSendButton;
    private DatabaseReference mFirebaseDatabaseReference;
    private EditText mMessageEditText;
    private EditText mMessageEditText1;
    //private Button msendButton;
    //TextView restaurantName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurant);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final String s = "Restaurants";

        mSendButton = (Button) findViewById(R.id.Rupload);
        mMessageEditText = (EditText) findViewById(R.id.Rname);
        mMessageEditText1 = (EditText) findViewById(R.id.Raddress);


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantName restaurantName = new
                        RestaurantName(mMessageEditText.getText().toString(),
                        mMessageEditText1.getText().toString()
                        );
                mFirebaseDatabaseReference.child(s)
                        .push().setValue(restaurantName);
                mMessageEditText.setText("");
                mMessageEditText1.setText("");
                //sendNotificationToUser(friendlyMessage.getName(), friendlyMessage.getText());
            }
        });

    }

}
