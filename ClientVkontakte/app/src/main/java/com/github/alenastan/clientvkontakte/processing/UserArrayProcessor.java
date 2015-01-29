package com.github.alenastan.clientvkontakte.processing;

import com.github.alenastan.clientvkontakte.bo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lena on 29.01.2015.
 */
public class UserArrayProcessor implements Processor<List<User>,InputStream>{

    @Override
    public List<User> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONObject("response").getJSONArray("items");
        List<User> noteArray = new ArrayList<User>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            User user = new User(jsonObject);
            user.initName();
            noteArray.add(user);
        }
        return noteArray;
    }

}
