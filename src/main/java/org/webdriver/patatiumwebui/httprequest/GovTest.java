package org.webdriver.patatiumwebui.httprequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.taskdefs.condition.Http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GovTest {
    private static String getCookie(Map<String, String> responseCookieMap) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry: responseCookieMap.entrySet()) {
            list.add(entry.getKey() + "=" + entry.getValue());
        }
        return StringUtils.join(list.toArray(), "; ");
    }

    private static void setResponseCookie(Map<String,List<String>> map, Map<String, String> responseCookieMap) {
        Pattern pattern = Pattern.compile("(.*?)=(.*?);");
        for (Map.Entry<String, List<String>> entry: map.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase("Set-Cookie")) {
                for (String item: entry.getValue()) {
                    Matcher matcher = pattern.matcher(item);
                    while (matcher.find()) {
                        responseCookieMap.put(matcher.group(1), matcher.group(2));
                    }
                }
            }
        }
    }

    private static String get(String urlString, Map<String, String> responseCookieMap) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String cookie = getCookie(responseCookieMap);
        if (StringUtils.isNotEmpty(cookie)) {
            connection.setRequestProperty("Cookie", cookie);
        }

        connection.connect();

        InputStream in = null;
        try {
            in = connection.getInputStream();

            setResponseCookie(connection.getHeaderFields(), responseCookieMap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int read = -1;
            while ((read = in.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            return byteArrayOutputStream.toString();
        } finally {
            if (in != null) {
                in.close();
            }
            connection.disconnect();
        }
    }

    private static String post(String urlString, String data, Map<String, String> responseCookieMap) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        String cookie = getCookie(responseCookieMap);
        if (StringUtils.isNotEmpty(cookie)) {
            connection.setRequestProperty("Cookie", cookie);
        }

        connection.connect();

        connection.getOutputStream().write(data.getBytes("UTF-8"));

        InputStream in = null;
        try {
            in = connection.getInputStream();

            setResponseCookie(connection.getHeaderFields(), responseCookieMap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int read = -1;
            while ((read = in.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            return byteArrayOutputStream.toString();
        } finally {
            if (in != null) {
                in.close();
            }
            connection.disconnect();
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> responseCookieMap = new HashMap<>();
        {
            String url = "http://wsbsfwt1.xmtax.gov.cn:8001/";
            String indexContent = get(url, responseCookieMap);
        }
        System.out.println(responseCookieMap);
        {
            String url = "http://wsbsfwt2.xmtax.gov.cn:8001/lhpt/clearSession.do";
            String indexContent = get(url, responseCookieMap);
        }
        System.out.println(responseCookieMap);
        {
//            /bsfw/login/checkLoginNew.do
            String url = "http://wsbsfwt1.xmtax.gov.cn:8001/bsfw/login/checkLoginNew.do";
            String data = "loginId=91350206303146883X&userPassword=Z2ZsMjM5Njg4OA%3D%3D&checkCode=993s";
            String content = post(url, data, responseCookieMap);
            System.out.println(content);
        }
        System.out.println(responseCookieMap);

    }
}
