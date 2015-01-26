package com.github.alenastan.clientvkontakte.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lena on 26.01.2015.
 */
public class Wall extends JSONObjectWrapper {

    private static String FROM_ID = "from_id";
    private static String DATE = "date";
    private static String TEXT = "text";
    private static String ID = "id";
    private static final String ATTACHMENTS = "attachments";
    private static final String TYPE = "type";
    private static final String PHOTO = "photo";
    private static final String LINK = "link";

    private static final String PHOTO_604 = "photo_604";
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String POSTER_ID = "owner_id";

    //INTERNAL
    private static final String NAME = "NAME";
    private String mImageUrl;
    private String mUrl;
    private String mUrlTitle;

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

    public Wall(JSONObject jsonObject) {
        super(jsonObject);
        if (jsonObject.has(ATTACHMENTS)) {
            try {
                JSONArray att = jsonObject.getJSONArray(ATTACHMENTS);
                for (int value = 0; value < att.length(); value++) {
                    JSONObject attachment = att.getJSONObject(value);
                    String type = attachment.getString(TYPE);
                    if (type.equals(PHOTO)) {
                        mImageUrl = attachment.getJSONObject(PHOTO).getString(PHOTO_604);
                    } else if (type.equalsIgnoreCase(LINK)) {
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
        return getString(DATE);
    }

//    public String getDate() {
//        return getString(DATE);
//    }

    public String getPhoto() {
        return getImageUrl();

    }

    public String getId() {
        return getString(ID);
    }

    public String getOwner_Id() throws Exception {
        return getString(POSTER_ID);
    }

    public Long getPosterId() {
        return Math.abs(getLong(POSTER_ID));
    }

    public String getFROM_ID() throws Exception {
        return getString(FROM_ID);
    }

    public String getImageUrl() {
        return mImageUrl;
    }


}