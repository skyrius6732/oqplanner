package com.oqplanner.common;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.ObjectUtils;

public class WebCrawling {

    public static Connection getJsoupConnection(String url) throws Exception {
        return Jsoup.connect(url);
    }

    public static Document getJsoupElements(Connection connection, String url) throws Exception {

        Connection conn = !ObjectUtils.isEmpty(connection) ? connection : getJsoupConnection(url);
        Document doc = conn.get();
        return doc;
    }
}
