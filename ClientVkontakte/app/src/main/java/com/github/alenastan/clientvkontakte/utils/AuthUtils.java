package com.github.alenastan.clientvkontakte.utils;

import com.github.alenastan.clientvkontakte.auth.VkOAuthHelper;

/**
 * Created by lena on 25.01.2015.
 */
public class AuthUtils {

    public static boolean isLogged() {
        return VkOAuthHelper.isLogged();
    }

}
