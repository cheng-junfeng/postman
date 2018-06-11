package com.postman.net.downUp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {
    /**
     * 对下载url进行转译编码(解决无法下载含有空格和中文名称的文件问题)
     *
     * @param url 要下载的url
     * @return null 如果转译失败;否则返回转译后的url
     */
    public static String downloadUrlEncode(String url) {
        try {
            String urlEncode = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
            urlEncode = urlEncode.replaceAll("%3A", ":").replaceAll("%2F", "/");

            return urlEncode;
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }
}
