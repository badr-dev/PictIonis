package com.example.badredinebelhadef.pictionis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.badredinebelhadef.pictionis.ui.Game;
import com.example.badredinebelhadef.pictionis.ui.OngoingGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.attr.host;

public class JoiningActivity extends AppCompatActivity {

    private ListView mListView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> hosts;
    private FirebaseAuth auth;
    private ArrayList<OngoingGame> games;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);

        auth    = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("hosted_games");

        mListView = (ListView) findViewById(R.id.listView);
        hosts = new ArrayList<String>();
        games = new ArrayList<OngoingGame>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hosts);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JoiningActivity.this, VisitorsActivity.class);
                OngoingGame game = games.get(position);
                intent.putExtra("game", game);
                startActivity(intent);
                finish();
            }
        });

        myRef.addChildEventListener(HostsListener);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    ValueEventListener GameListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot GamePlayerSnapshot: dataSnapshot.getChildren()) {
                OngoingGame game = GamePlayerSnapshot.getValue(OngoingGame.class);
                hosts.add(game.getHostName());
                games.add(game);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("listener", "loadPost:onCancelled", databaseError.toException());
        }
    };

    ChildEventListener HostsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            OngoingGame game = dataSnapshot.getValue(OngoingGame.class);
            if (game.getHostName() == null || TextUtils.isEmpty(game.getHostName()) ){
                game.setHostName("Nameless room");
            }
            hosts.add(game.getHostName());
            games.add(game);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
