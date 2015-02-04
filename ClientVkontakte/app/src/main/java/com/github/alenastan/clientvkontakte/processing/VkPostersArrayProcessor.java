package com.github.alenastan.clientvkontakte.processing;

import com.github.alenastan.clientvkontakte.bo.VkPoster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lena on 29.01.2015.
 */
public class VkPostersArrayProcessor {

    private static final String GROUPS = "groups";
    private static final String GROUPS_TITLE = "name";
    private static final String GROUP_ID = "id";
    private static final String GROUPS_AVATAR = "photo_100";
    private static final String PROFILES = "profiles";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String USER_PHOTO = "photo_100";
    private static final String USER_ID = "id";

    private JSONObject mJSONObject;
    private Map<Long, VkPoster> mUserMap;
    
    public VkPostersArrayProcessor(JSONObject jsonObject) {
        mJSONObject = jsonObject;mUserMap = new ConcurrentHashMap<>();
    }


    public void process() throws Exception {
        JSONArray groups = mJSONObject.optJSONArray(GROUPS);
        if (groups != null) {
            for (int i = 0; i < groups.length(); i++) {
                JSONObject group = groups.getJSONObject(i);
                String title = group.getString(GROUPS_TITLE);
                String avatarUrl = group.getString(GROUPS_AVATAR);
                VkPoster poster = new VkPoster(title, avatarUrl);
                mUserMap.put(group.optLong(GROUP_ID), poster);
            }
        }

        JSONArray profiles = mJSONObject.optJSONArray(PROFILES);
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
    }

    private String getFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    public VkPoster getPoster(Long id) {
        return mUserMap.get(id);
    }

}
