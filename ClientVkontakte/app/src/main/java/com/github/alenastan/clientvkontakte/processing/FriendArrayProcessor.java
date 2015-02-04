package com.github.alenastan.clientvkontakte.processing;

import android.content.Context;

import com.github.alenastan.clientvkontakte.bo.Friend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lena on 25.01.2015.
 */
public class FriendArrayProcessor implements Processor<List<Friend>,InputStream>{
    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";

    @Override
    public List<Friend> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONObject(RESPONSE).getJSONArray(ITEMS);
        List<Friend> noteArray = new ArrayList<Friend>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            Friend friend = new Friend(jsonObject);
            friend.initName();
            noteArray.add(friend);
        }
        return noteArray;
    }

}