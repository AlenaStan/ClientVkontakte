package com.github.alenastan.clientvkontakte;


import com.github.alenastan.clientvkontakte.bo.User;

/**
 * Created by lena on 25.01.2015.
 */
public class Api {
    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4748843&scope=offline,wall,photos,status&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";
    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String FRIENDS_GET = BASE_PATH + "friends.get?fields=photo_200_orig,online,nickname";
    public static final String WALL_GET = BASE_PATH + "wall.get?filters=owner&fields=photo_100&extended=1";
    public static final String NEWS_GET = BASE_PATH + "newsfeed.get?filters=post&fields=photo_130"+"&count=50";
    public static final String USER_GET = BASE_PATH + "users.get?fields=photo_50&user_ids=";

}
