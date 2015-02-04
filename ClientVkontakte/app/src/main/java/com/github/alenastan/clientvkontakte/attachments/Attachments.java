package com.github.alenastan.clientvkontakte.attachments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lena on 01.02.2015.
 */
public class Attachments implements Attachment {

    private static final String TYPE = "type";
    public static final String PHOTO = "photo";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";


    private List<Attachment> mAttachments;

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    public Attachments(JSONArray attachments) throws JSONException {
        mAttachments = new ArrayList<>();
        for (int value = 0; value < attachments.length(); value++) {
            Attachment att = getAttachment(attachments.getJSONObject(value));
            if (att != null) {
                mAttachments.add(att);
            }
        }
    }

    public List<Attachment> getAttachments() {
//        if (mAttachments == null) {
//            return null; //Collections.emptyList();
//        }
        return mAttachments;
    }

    private Attachment getAttachment(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            throw new IllegalArgumentException();
        }
        switch (jsonObject.optString(TYPE)) {
            case (PHOTO): {
                return new Photo(jsonObject.getJSONObject(PHOTO));
            }
            case (VIDEO): {
                return new Video(jsonObject.getJSONObject(VIDEO));
            }
            case (AUDIO): {
                return new Audio(jsonObject.getJSONObject(AUDIO));
            }
            default: {
                return null;
            }
        }
    }
}
