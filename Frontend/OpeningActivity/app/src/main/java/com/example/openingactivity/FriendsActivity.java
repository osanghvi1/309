package com.example.openingactivity;

import static java.lang.System.in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Friend {
    String username;
    int userID;
    //constructor
    public Friend(String username, int userID) {
        this.username = username;
        this.userID = userID;
    }
}

public class FriendsActivity extends AppCompatActivity implements Request {
    // UI elements
    private float currentRotation = 0f;
    Button buttonBack, buttonFindFriends;
    ImageButton buttonRefreshFriends;
    ListView lvFriends;
    ExecutorService executorService;
    ArrayList<Friend> friendsList = new ArrayList<>();
    ArrayAdapter<Friend> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        buttonBack = findViewById(R.id.button_friends_back);
        buttonFindFriends = findViewById(R.id.button_find_friends);
        buttonRefreshFriends = findViewById(R.id.button_refresh_friends);


        // Initialize the ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        lvFriends = findViewById(R.id.friends_list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, friendsList);
        lvFriends.setAdapter(adapter);

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend selectedFriend = friendsList.get(position);
                // TODO enter chat with selectedFriend
            }
        });



        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonFindFriends.setOnClickListener(v -> {
            // Handle friend finder button click
            Intent myIntent = new Intent(this, FriendFinderActivity.class);
            startActivity(myIntent);
        });

        buttonRefreshFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For button rotation
                currentRotation += 360f;
                buttonRefreshFriends.animate().rotation(currentRotation).setDuration(800).start();

                // TODO will just redo the GET friends task and repopulate the arraylist

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String response = sendRequest("GET", "/friends/" + user.getUserID(), null);
                        // returns String of usernames and userIDS
                        // parse the string for each combo and put into the friendList
                    }
                });

                // parse the response into the arrayList - dummy info for now
                friendsList.add(new Friend("Person 1", 1));
                friendsList.add(new Friend("Person 2", 2));
                friendsList.add(new Friend("Person 3", 3));
                friendsList.add(new Friend("Person 4", 4));


                // update the ListView
                adapter.notifyDataSetChanged();
            }
        });
    }
}
