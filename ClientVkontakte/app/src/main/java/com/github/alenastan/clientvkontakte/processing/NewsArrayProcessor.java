package com.github.alenastan.clientvkontakte.processing;

import android.content.Context;

import com.github.alenastan.clientvkontakte.bo.News;
import com.github.alenastan.clientvkontakte.bo.VkPoster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lena on 26.01.2015.
 */
public class NewsArrayProcessor implements Processor<List<News>,InputStream> {

    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";
    private static final String GROUPS = "groups";
    private static final String GROUPS_TITLE = "name";
    private static final String GROUP_ID = "id";
    private static final String GROUPS_AVATAR = "photo_100";
    private static final String PROFILES = "profiles";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String USER_PHOTO = "photo_100";
    private static final String USER_ID = "id";
    private static final String NEXT_FROM = "next_from";

    private ConcurrentHashMap<Long, VkPoster> mUserMap;
    private DateFormat mDateFormat;
    private Context mContext;

    public NewsArrayProcessor (Context context) {
        mContext = context;
        mUserMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<News> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject response = new JSONObject(string).getJSONObject(RESPONSE);
        JSONArray groups = response.getJSONArray(GROUPS);
        if (groups != null) {
            for (int i = 0; i < groups.length(); i++) {
                JSONObject group = groups.getJSONObject(i);
                String title = group.getString(GROUPS_TITLE);
                String avatarUrl = group.getString(GROUPS_AVATAR);
                VkPoster poster = new VkPoster(title, avatarUrl);
                mUserMap.put(group.getLong(GROUP_ID), poster);
            }
        }
        JSONArray profiles = response.getJSONArray(PROFILES);
        if (profiles != null) {
            for (int i = 0; i < profiles.length(); i++) {
                JSONObject profile = profiles.getJSONObject(i);
                String firstName = profile.getString(FIRST_NAME);
                String lastName = profile.getString(LAST_NAME);
                String fullName = getFullName(firstName, lastName);
                String url = profile.getString(USER_PHOTO);
                VkPoster poster = new VkPoster(fullName, url);
                mUserMap.put(profile.getLong(USER_ID), poster);
            }
        }
        JSONArray news = response.getJSONArray(ITEMS);
        String nextNewsId = response.getString(NEXT_FROM);
        List<News> noteArray = new ArrayList<News>(news.length());
        mDateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        for (int i = 0; i < news.length(); i++) {
            JSONObject jsonObject = news.getJSONObject(i);
            News vkNews = new News(jsonObject, mDateFormat);
            vkNews.addVkPosterInfo(getVkPoster(Math.abs(vkNews.getSourceId())));
            noteArray.add(vkNews);
        }
        return noteArray;
    }
    private String getFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    public VkPoster getVkPoster(Long id) {
        return mUserMap.get(id);
    }

}
