package com.example.badredinebelhadef.pictionis.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by antoine on 11/05/17.
 */

public class OngoingGame implements Serializable {

    private String hostName;
    public String drawing;
    public String word;
    public String userId;
    public Map<String, Message> messages;
    private NavigableMap<String, Message> fmessages;

    public OngoingGame() {
        fmessages = new TreeMap<String, Message>();
    }

    public String getLastMessageToString()
    {
        fmessages = new TreeMap<String, Message>();
        fmessages.putAll(messages);
        return fmessages.lastEntry().toString();
    }

    public ArrayList<String> getAllMessageArrayList()
    {
        ArrayList<String> temp = new ArrayList<String>();
        Iterator it = messages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Message message = (Message) pair.getValue();
            temp.add(message.sender + "=" + message.text);
        }

        return temp;
    }
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String name){
        hostName = name;
    }
}
