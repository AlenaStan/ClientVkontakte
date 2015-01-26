package com.github.alenastan.clientvkontakte.processing;

import com.github.alenastan.clientvkontakte.bo.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lena on 26.01.2015.
 */
public class NewsArrayProcessor implements Processor<List<News>,InputStream> {

    @Override
    public List<News> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONObject("response").getJSONArray("items");
        List<News> noteArray = new ArrayList<News>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            News news = new News(jsonObject);
            noteArray.add(news);
        }
        return noteArray;
    }
}
