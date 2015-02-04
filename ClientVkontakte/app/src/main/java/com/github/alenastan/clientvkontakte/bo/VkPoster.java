package com.github.alenastan.clientvkontakte.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by lena on 29.01.2015.
 */
public class VkPoster {

    private String mPhotoUrl;
    private String mName;

    public VkPoster (String name, String url) {
        mName = name;
        mPhotoUrl = url;
    }

    public  String getName(){
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

}
