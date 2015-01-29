package com.github.alenastan.clientvkontakte.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Created by lena on 26.01.2015.
 */
public class Wall extends JSONObjectWrapper {

    private static String FROM_ID = "from_id";
    private static String DATE = "date";
    private static String TEXT = "text";
    private static String ID = "id";
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String PHOTO = "photo";
    private static final String LINK = "link";
    private static final String PHOTO_604 = "photo_604";
    private static final String POSTER_ID = "owner_id";
    private static final String ATTACHMENTS = "attachments";
    private static final String TYPE = "type";


    private String mImageUrl;
    private String mUrl;
    private String mUrlTitle;
    private String mPostDate;
    private String mUserPhoto;
    private String mUserName;

    public static final Parcelable.Creator<Wall> CREATOR
            = new Parcelable.Creator<Wall>() {
        public Wall createFromParcel(Parcel in) {
            return new Wall(in);
        }

        public Wall[] newArray(int size) {
            return new Wall[size];
        }
    };


    public Wall(String jsonObject) {
        super(jsonObject);
    }

    public Wall(JSONObject jsonObject){
        super(jsonObject);
            if (jsonObject.has(ATTACHMENTS)) {
                try {
                    JSONArray att = jsonObject.getJSONArray(ATTACHMENTS);
                    for (int value = 0; value < att.length(); value++) {
                        JSONObject attachment = att.getJSONObject(value);
                        String name = attachment.getString(TYPE);
                        if (name.equals(PHOTO)) {
                            mImageUrl = attachment.getJSONObject(PHOTO).getString(PHOTO_604);
                        } else if (name.equals(LINK)) {
                            mUrl = attachment.getJSONObject(LINK).getString(URL);
                            mUrlTitle = attachment.getJSONObject(LINK).getString(TITLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }

    protected Wall(Parcel in) {
        super(in);
    }

    public String getText()  {
        return getString(TEXT);
    }

    public String getDate() {
        return mPostDate;
    }

    public String getPhoto() {
        return getImageUrl();

    }
    public String getId() {
        return getString(ID);
    }

    public String getOwnerId() throws Exception {
        return getString(POSTER_ID);
    }

    public Long getPosterId() { return getLong(POSTER_ID);}

   public String getUserPhoto() { return mUserPhoto; }

   public String getUserName() { return mUserName; }

    public String getFromId() throws Exception {
        return getString(FROM_ID);
    }

    public String getImageUrl() {
        return mImageUrl;
    }



}