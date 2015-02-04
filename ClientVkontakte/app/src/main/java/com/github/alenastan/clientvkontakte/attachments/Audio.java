package com.github.alenastan.clientvkontakte.attachments;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lena on 01.02.2015.
 */
public class Audio implements Attachment{

    private static final String ID = "id";
    private static final String URL = "url";
    private static final String OWNER_ID = "owner_id";
    private static final String TITLE = "title";

    private long mId;
    private String mTitle;
    private long mOwnerId;
    private String mUrl;

    public Audio (JSONObject jsonObject) throws JSONException {

        mId = jsonObject.getLong(ID);
        mOwnerId = jsonObject.getLong(OWNER_ID);
        mTitle = jsonObject.getString(TITLE);
        mUrl = jsonObject.getString(URL);

    }

    public long getId() { return mId; }

    public String getUrl() { return mUrl; }

    public long getOwnerId() { return mOwnerId; }

    public String getTitle() { return mTitle; }

    public String getType() { return Attachments.AUDIO; }

 }
