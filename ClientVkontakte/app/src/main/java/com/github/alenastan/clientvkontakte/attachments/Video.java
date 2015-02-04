package com.github.alenastan.clientvkontakte.attachments;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lena on 01.02.2015.
 */
public class Video implements Attachment {

    private static final String ID = "id";
    private static final String OWNER_ID = "owner_id";
    private static final String TEXT = "description";
    private static final String TITLE = "title";
    private static final String PHOTO = "photo_320";
    private static final String URL = "link";
    private static final String DATE = "date";

    @Override
    public String getType() {
        return null;
    }

    private long mId;
    private long mOwnerId;
    private String mText;
    private String mPhoto;
    private String mTitle;
    private String mUrl;
    private String mDate;

public Video (JSONObject jsonObject) throws JSONException {

    mId = jsonObject.optLong(ID);
    mText = jsonObject.optString(TEXT);
    mDate = jsonObject.optString(DATE);
    mUrl = jsonObject.optString(URL);
    mPhoto = jsonObject.optString(PHOTO);
    mTitle = jsonObject.optString(TITLE);
    mOwnerId = jsonObject.optLong(OWNER_ID);
 }

    public long getId() { return mId; }

    public long getOwnerId() { return mOwnerId; }

    public String getUrl() { return mUrl; }

    public String getText() { return mText; }

    public String getDate() { return mDate; }

    public String getPhoto() { return mPhoto; }

    public String getTitle() { return mTitle; }

    public String getAttachmentType() { return Attachments.VIDEO; }

}

