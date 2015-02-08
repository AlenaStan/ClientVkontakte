package com.github.alenastan.clientvkontakte.bo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.alenastan.clientvkontakte.attachments.Attachment;
import com.github.alenastan.clientvkontakte.attachments.Attachments;
import com.github.alenastan.clientvkontakte.attachments.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;

/**
 * Created by lena on 26.01.2015.
 */
public class News  extends JSONObjectWrapper {

    private static final String DATE = "date";
    private static final String TEXT = "text";
    private static final String CAN_COMMENT = "can_post";
    private static final String COMMENTS = "comments";
    private static final String ID = "post_id";
    private static final String SOURCE_ID = "source_id";
    private static final String ATTACHMENTS = "attachments";

    private String mImageUrl;
    private String mUserPhoto;
    private Attachments mAttaches;
    private String mUserName;
    private String mFormatDate;
    private  JSONObject mCanComment;

    public static final Parcelable.Creator<News> CREATOR
            = new Parcelable.Creator<News>() {
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

//    public News(String jsonObject) {
//        super(jsonObject);
//    }

    public News(JSONObject jsonObject, DateFormat format) {
        super(jsonObject);
        String date = getDate();
        formatDate(date, format);
        JSONObject comments = getComments(jsonObject);
        mCanComment = getCanComment(comments);
        if (jsonObject.has(ATTACHMENTS)) {
            try {
                mAttaches = new Attachments(jsonObject.getJSONArray(ATTACHMENTS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (Attachment attachment : mAttaches.getAttachments()) {
                if (attachment instanceof Photo) {
                    mImageUrl = attachment.getUrl();
                    break;
                }
            }
        }

    }

    protected News(Parcel in) {
        super(in);
    }

    public String getText()  {
        return getString(TEXT);
    }

    public String getDate() {
        return getString(DATE);
    }

    public  String formatDate(String date, DateFormat format){
        if(date != null){
            java.util.Date time = new java.util.Date(Long.parseLong(date) * 1000);
            mFormatDate = format.format(time);
            return mFormatDate;
        }
        return  null;
    }

    public String getPhoto() { return mImageUrl; }

    public String getPostId() {
        return getString(ID);
    }

    public Long getSourceId() throws Exception {
        return getLong(SOURCE_ID);
    }

    public String getUserPhoto() { return mUserPhoto; }

    public String getUserName() { return mUserName; }

    public JSONObject getComments(JSONObject jsonObject) {

        try {
            return jsonObject.getJSONObject(COMMENTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public JSONObject getCanComment(JSONObject jsonObject) {
        JSONObject comments = jsonObject;
        if (comments!= null){
            try {
                return comments.getJSONObject(CAN_COMMENT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void addVkPosterInfo (VkPoster poster) {
        mUserPhoto = poster.getPhotoUrl();
        mUserName = poster.getName();
    }

    public JSONObject getCanComment() {
        return mCanComment;
    }

    public String getFormatDate() {
        return mFormatDate;
    }
}