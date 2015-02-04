package com.github.alenastan.clientvkontakte.processing;

import android.content.Context;

import com.github.alenastan.clientvkontakte.bo.Wall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lena on 26.01.2015.
 */
public class WallArrayProcessor implements Processor<List<Wall>,InputStream> {

    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";
    private Context mContext;
    private DateFormat mDateFormat;

    public WallArrayProcessor (Context context) {

        mContext = context;

    }

    @Override
    public List<Wall> process(InputStream inputStream) throws Exception {
       String string = new StringProcessor().process(inputStream);
        JSONObject response = new JSONObject(string).getJSONObject(RESPONSE);
        JSONArray wall = response.getJSONArray(ITEMS);
        VkPostersArrayProcessor posters = new VkPostersArrayProcessor(response);
        posters.process();
        List<Wall> noteArray = new ArrayList<Wall>(wall.length());
        mDateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        for (int i = 0; i < wall.length(); i++) {
            JSONObject jsonObject = wall.getJSONObject(i);
            Wall vkWall = new Wall(jsonObject, mDateFormat);
            vkWall.addVkPosterInfo(posters.getPoster(Math.abs(vkWall.getFromId())));
            noteArray.add(vkWall);
        }
        return noteArray;
    }

 }