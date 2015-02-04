package com.github.alenastan.clientvkontakte.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.alenastan.clientvkontakte.attachments.Attachment;
import com.github.alenastan.clientvkontakte.attachments.Attachments;
import com.github.alenastan.clientvkontakte.attachments.Photo;

import org.json.JSONObject;

import java.text.DateFormat;

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
    private long mPosterId;


    private String mImageUrl;
    private String mDate;
    private String mPostDate;
    private String mUserPhoto;
    private String mUserName;
    private Attachments mAttaches;

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

    public Wall(JSONObject jsonObject, DateFormat ft){
        super(jsonObject);
        try {
        mPosterId = jsonObject.optLong(POSTER_ID);
        mPostDate = jsonObject.getString(DATE);
        java.util.Date time = new java.util.Date( Long.parseLong(mPostDate) * 1000);
        mDate = ft.format(time);
            if (jsonObject.has(ATTACHMENTS)) {
                mAttaches = new Attachments(jsonObject.getJSONArray(ATTACHMENTS));

                mAttaches = new Attachments(jsonObject.getJSONArray(ATTACHMENTS));

                    for (Attachment attach : mAttaches.getAttachments()){
                        if (attach instanceof Photo){
                            mImageUrl = attach.getUrl();

                            break;
                        }
                    }
                }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected Wall(Parcel in) {
        super(in);
    }

    public String getText()  {
        return getString(TEXT);
    }

    public String getDate() {
        return mDate;
    }

    public String getImage() {
        return getImageUrl();

    }
    public String getId() {
        return getString(ID);
    }

//    public String getOwnerId() throws Exception {
//        return getString(POSTER_ID);
//    }


    public Long getPosterId() { return mPosterId;}

    public String getUserPhoto() { return mUserPhoto; }

    public String getUserName() { return mUserName; }

    public Long getFromId() throws Exception {
        return getLong(FROM_ID);
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public Attachments getAttaches(){ return mAttaches;}

    public void addVkPosterInfo (VkPoster poster) {
        mUserPhoto = poster.getPhotoUrl();
        mUserName = poster.getName();
    }

}