package com.github.alenastan.clientvkontakte.auth;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.github.alenastan.clientvkontakte.Api;

import org.apache.http.auth.AuthenticationException;

/**
 * Created by lena on 25.01.2015.
 */
public class VkOAuthHelper {

    public static interface Callbacks {

        void onError(Exception e);
        void onSuccess();

    }

    private static String sToken;
    private static final String TAG = VkOAuthHelper.class.getSimpleName();


    public static boolean isLogged() {
        return !TextUtils.isEmpty(sToken);
    }

    public static boolean parseUrl(Activity activity, String url, Callbacks callbacks) {
        if (url.startsWith(Api.REDIRECT_URL)) {
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
            String accessToken = parsedFragment.getQueryParameter("access_token");
            if (!TextUtils.isEmpty(accessToken)) {
                Log.d(TAG, "token " + accessToken);
                sToken = accessToken;
                callbacks.onSuccess();
                return true;
            } else {
                String error = parsedFragment.getQueryParameter("error");
                String errorDescription = parsedFragment.getQueryParameter("error_description");
                String errorReason = parsedFragment.getQueryParameter("error_reason");
                if (!TextUtils.isEmpty(error)) {
                    callbacks.onError(new AuthenticationException(error + ", reason : " + errorReason + "(" + errorDescription + ")"));
                    return false;
                }

            }
        }
        return false;
    }
    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&access_token="+sToken;
        } else {
            return url + "?access_token="+sToken;
        }
    }

}