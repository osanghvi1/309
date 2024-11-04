package com.example.openingactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class FriendFinderActivity extends AppCompatActivity implements Request {

    Button buttonBack;
    ListView lvEveryone;
    ExecutorService executorService;

    ArrayList<Friend> peopleList = new ArrayList<>();
    ArrayAdapter<Friend> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_finder);
        buttonBack = findViewById(R.id.button_friends_back);
        executorService = Executors.newSingleThreadExecutor();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO GET method for friends
        // Get method, get all users
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String response = sendRequest("GET", "/users", null);
                //for each user in the response string, add them to peopleList
                String[] users = response.split(";");
                for (int i = 0; i < users.length; i++) {
                    peopleList.add(new Friend("username", "id"));
                }
            }
        });



        lvEveryone = findViewById(R.id.friends_list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, peopleList);
        lvEveryone.setAdapter(adapter);

        lvEveryone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend selectedFriend = peopleList.get(position);
                peopleList.remove(selectedFriend);
                adapter.notifyDataSetChanged();
                Toast.makeText(FriendFinderActivity.this, "Friend removed: " + selectedFriend.username, Toast.LENGTH_SHORT).show();

                // POST request to add selectedFriend to friendsList
                JSONObject json = new JSONObject();

                try {
                    json.put("friendID", selectedFriend.userID);
                    json.put("friendUsername", selectedFriend.username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String response = sendRequest("POST", "/friends/" + user.getUserID(), json.toString());
                        // put response in log for debugging
                        // TODO have Om implement a null return if user is already friended.
                        if (response == null) {
                            Toast.makeText(FriendFinderActivity.this, "Friend already added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
