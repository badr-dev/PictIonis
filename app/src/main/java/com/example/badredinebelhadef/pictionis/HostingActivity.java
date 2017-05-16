package com.example.badredinebelhadef.pictionis;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badredinebelhadef.pictionis.ui.DrawingView;
import com.example.badredinebelhadef.pictionis.ui.Game;
import com.example.badredinebelhadef.pictionis.ui.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import static android.R.attr.duration;

public class HostingActivity extends AppCompatActivity {

    private DrawingView drawView;
    private ImageButton currPaint;
    private FirebaseAuth auth;
    private Game game;
    private FirebaseDatabase database;
    private DatabaseReference myRefMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);
        drawView = (DrawingView) findViewById(R.id.drawingPanel);


        game = Game.getInstance();

        game.setHostName(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]);
        game.saveToFirebase();

        database = FirebaseDatabase.getInstance();
        myRefMessages = database.getReference("hosted_games/" + game.getUserId() + "/messages");

        Button clearButton = (Button) findViewById(R.id.cleanButton);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setStringDrawing("empty");
                drawView.clear();
            }
        });


        myRefMessages.addChildEventListener(messageListener);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close session")
                .setMessage("SEssion will shut down !")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //game.destroyInstance();
                        myRefMessages.removeEventListener(messageListener);
                        finish();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    ChildEventListener messageListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Message msg = dataSnapshot.getValue(Message.class);

            Toast toast = Toast.makeText(getBaseContext(), msg.text, Toast.LENGTH_LONG);
            toast.show();
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