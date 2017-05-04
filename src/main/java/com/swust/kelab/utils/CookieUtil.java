package com.swust.kelab.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

public class CookieUtil {
    public static String findCookieValue(Cookie[] cookies, String cookieKey) {
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieKey)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    // 避免重复的劳动
    public static void addCookieToResponse(String name, String value, String path, String domain, int maxAge,
            HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static String encode(String str, String encode) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, encode);
    }

    public static String decode(String str, Charset encode) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, encode.displayName());
    }
}
