package com.example.badredinebelhadef.pictionis;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.badredinebelhadef.pictionis.ui.Game;
import com.example.badredinebelhadef.pictionis.ui.Message;
import com.example.badredinebelhadef.pictionis.ui.OngoingGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VisitorsActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRefMessages;
    private DatabaseReference myRefDrawing;
    private DatabaseReference myRefWord;
    private ArrayList<String> chatMessages;
    private ArrayAdapter<String> adapter;
    private ListView mListView;
    private ImageView myImage;
    private EditText editText;
    private OngoingGame game;
    private FirebaseAuth auth;
    private String currentUserNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);
        Intent intent = getIntent();
        game = (OngoingGame) intent.getSerializableExtra("game");
        chatMessages    = new ArrayList<String>();
        chatMessages    = game.getAllMessageArrayList();
        auth        = FirebaseAuth.getInstance();
        mListView   = (ListView) findViewById(R.id.chat_text);
        editText    = (EditText) findViewById(R.id.edit_text);
        myImage     = (ImageView) findViewById(R.id.image_draw);
        editText.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);

        database = FirebaseDatabase.getInstance();
        currentUserNickname = auth.getCurrentUser().getEmail().split("@")[0];
        myRef = database.getReference("hosted_games/" + game.userId);
        myRefMessages = database.getReference("hosted_games/" + game.userId + "/messages");
        myRefDrawing    = database.getReference("hosted_games/" + game.userId + "/drawing");
        myRefWord    = database.getReference("hosted_games/" + game.userId + "/word");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatMessages);
        mListView.setAdapter(adapter);
        sendConnectionMessage();

        myRefMessages.addValueEventListener(messageListener);
        myRefDrawing.addValueEventListener(drawingListener);
        myRefWord.addValueEventListener(wordListener);
    }

    public void changeImage()
    {
        if (!game.drawing.equals("empty")){
            byte[] decodedString = Base64.decode(game.drawing, Base64.NO_WRAP);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            myImage.setImageBitmap(decodedByte);
        }
    }

    public void sendMessage(View view)
    {
        Message message = new Message();
        ArrayList<Message> messages = new ArrayList<Message>();
        message.sender = currentUserNickname;
        message.text = editText.getText().toString().trim();
        editText.setText("");
        Log.v("gameplayerword:", game.word);
        Log.v("typedword:", message.text);
        if (message.text.trim().equalsIgnoreCase(game.word)){
            new AlertDialog.Builder(VisitorsActivity.this)
                    .setTitle("Victoire")
                    .setMessage("Vous venez de trouver le bon mot !")
                    .setPositiveButton("Génial !", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        messages.add(message);
        myRefMessages.push().setValue(message);
    }

    public void sendConnectionMessage()
    {
        Message message = new Message();
        message.sender    = "";
        message.text     = currentUserNickname + " à rejoint la partie !";
        myRefMessages.push().setValue(message);
    }

    public void sendDisconnectMessage()
    {
        Message message = new Message();
        message.sender    = "";
        message.text     = currentUserNickname + " à quitté la partie !";
        myRefMessages.push().setValue(message);
    }

    @Override
    public void onBackPressed() {
        myRefMessages.removeEventListener(messageListener);
        myRefDrawing.removeEventListener(drawingListener);
        myRefWord.removeEventListener(wordListener);
        sendDisconnectMessage();
        super.onBackPressed();
    }

    ValueEventListener messageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            chatMessages.clear();
            for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                Message message = messageSnapshot.getValue(Message.class);
                chatMessages.add(message.sender + "=" + message.text);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("listener", "loadMessage:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener drawingListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            game.drawing = dataSnapshot.getValue().toString();
            changeImage();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    ValueEventListener wordListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot == null || dataSnapshot.getValue() == null ){
                game.word = "game word";
            }else{
                game.word = dataSnapshot.getValue().toString();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}
