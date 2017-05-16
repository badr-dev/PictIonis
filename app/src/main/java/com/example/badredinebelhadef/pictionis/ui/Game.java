package com.example.badredinebelhadef.pictionis.ui;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by antoine on 11/05/17.
 */

public class Game extends Application {

    private String drawing;
    private String hostName;
    private String userId;
    private byte[] byteArray;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRefDrawing;
    private DatabaseReference myRefMessages;

    private static Game INSTANCE = null;

    public static Game getInstance()
    {
        if (INSTANCE == null){
            INSTANCE = new Game();
        }
        return INSTANCE;
    }

    public void destroy()
    {
        if (INSTANCE != null){
            INSTANCE = null;
        }
    }

    public void saveToFirebase()
    {
        myRef.child(userId).setValue(toMap());
        Message message = new Message();
        message.text = "Let's draw !!";
        message.sender = hostName;
        myRefMessages.push().setValue(message);
    }

    public void saveDrawingToFirebase()
    {
        myRefDrawing.setValue(drawing);
    }

    private Game()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("hosted_games");
        userId = auth.getCurrentUser().getUid();
        hostName = auth.getCurrentUser().getEmail().split("@")[0];
        drawing = "empty";
        myRefDrawing = database.getReference("hosted_games/" + userId + "/drawing");
        myRefMessages = database.getReference("hosted_games/" + userId + "/messages");
    }

    public void setStringDrawing(String draw){
        drawing = draw;
    }

    public String getUserId()
    {
        return userId;
    }

    public Game setDrawingFromBitmap(Bitmap bitmap)
    {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byteArray = stream.toByteArray();
        drawing = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        return this;
    }


    public void setHostName(String str){

        hostName = str;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("hostname", hostName);
        result.put("drawing", drawing);
        result.put("userId", userId);

        return result;
    }
}