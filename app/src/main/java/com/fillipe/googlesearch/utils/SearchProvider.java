package com.fillipe.googlesearch.utils;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchProvider {
    public static void search(SearchConfig config, SearchListener listener) {
        String url = getURL(config);
        String html = getHtmlFromUrl(url);

        if (html == null) {
            listener.onError();
            return;
        }

        ArrayList<HashMap<String, String>> pages = getPages(html);
        listener.onSuccess(pages);
    }

    public static String getURL(SearchConfig config) {
        if (config.params == null) config.params = new HashMap<>();
        config.params.put("start", String.valueOf(config.page * 10));
        config.params.put("q", config.query);

        StringBuilder urlBuilder = new StringBuilder("https://www.google.com/search?");
        for (String key : config.params.keySet()) {
            urlBuilder
                    .append(key)
                    .append("=")
                    .append(config.params.get(key))
                    .append("&");
        }

        String url = urlBuilder.toString();
        url = url.substring(0, url.length() - 1);
        return url;
    }

    public static String getHtmlFromUrl(String sUrl) {
        allowThreadPolicy();

        try {
            URL url = new URL(sUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            if (sb.length() == 0) {
                return null;
            }

            urlConnection.disconnect();

            try {
                reader.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<HashMap<String, String>> getPages(String html) {
        Pattern pattern = Pattern.compile("<div class=\"kvH3mc(.*?)<div class=\"VwiC3b.*?</div>");
        Matcher matcher = pattern.matcher(html);

        ArrayList<HashMap<String, String>> pages = new ArrayList<>();
        while (matcher.find()) {
            HashMap<String, String> page = getPage(matcher.group());
            if (page != null) pages.add(page);
        }

        return pages;
    }

    private static HashMap<String, String> getPage(String htmlPage) {
        String url = match(htmlPage, "role=\"text\">(.*?)</?span");
        String title = match(htmlPage, "role=\"link\">(.*?)</div>");
        String content = match(htmlPage, "style=\"-webkit-line-clamp(.*?)</div>");

        if (url.equals("") || Objects.equals(title, "") || content.equals("")) {
            return null;
        }

        HashMap<String, String> page = new HashMap<>();
        page.put("url", url);
        page.put("title", title);
        page.put("content", content);

        return page;
    }

    private static String match(String s, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            try {
                return URLDecoder.decode(matcher.group()
                        .replaceAll("<(.*?)>", "")
                        .replaceAll("(.*?)>", "")
                        .replaceAll("<(.*?)", ""), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return matcher.group()
                        .replaceAll("<(.*?)>", "")
                        .replaceAll("(.*?)>", "")
                        .replaceAll("<(.*?)", "");
            }
        }

        return "";
    }

    private static void allowThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
