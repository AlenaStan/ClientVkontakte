package com.github.alenastan.clientvkontakte.attachments;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lena on 01.02.2015.
 */
public class Photo implements Attachment{
    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String OWNER_ID = "owner_id";
    private static final String DATE = "date";
    private static final String URL_604 = "photo_604";
    private static final String ALBUM_ID = "album_id";

//    private static final String WIDTH = "width";
//    private static final String HEIGHT = "height";

    private long mId;
    private long mAlbumId;
    private long mOwnerId;
    private String mText;
    private String mUrl;
    private String mDate;


//    private int mWidth;
//    private int mHeight;

    public Photo(JSONObject jsonObject) throws JSONException {
        mId = jsonObject.getLong(ID);
        mDate = jsonObject.getString(DATE);
        mUrl = jsonObject.getString(URL_604);
        mAlbumId = jsonObject.getLong(ALBUM_ID);
        mOwnerId = jsonObject.getLong(OWNER_ID);
        mText = jsonObject.getString(TEXT);
//        mWidth = jsonObject.getInt(WIDTH);
//        mHeight = jsonObject.getInt(HEIGHT);
    }

    public long getId() { return mId; }

    public String getUrl() { return mUrl; }

    public long getAlbumId() { return mAlbumId; }

    public String getText() { return mText; }

    public String getDate() { return mDate; }

    public long getOwnerId() { return mOwnerId; }

//    public String getTitle() { return null; }
//
//    public String getPhoto() { return null; }

//    @Override
//    public String getType(JSONObject jsonObject) throws JSONException {
//        return super.getType(jsonObject);
//    }

    public String getType() { return Attachments.PHOTO; }


//    public int getWidth() {
//        return mWidth;
//    }
//
//
//    public int getHeight() {
//        return mHeight;
   // }
}

