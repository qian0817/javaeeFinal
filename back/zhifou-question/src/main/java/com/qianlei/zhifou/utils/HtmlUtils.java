package com.qianlei.zhifou.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** @author qianlei */
public class HtmlUtils {
  public static String cleanHtml(String html, Whitelist whitelist) {
    return Jsoup.clean(html, whitelist);
  }

  public static String cleanHtmlPlain(String html) {
    return cleanHtml(html, Whitelist.none());
  }

  public static String cleanHtmlRelaxed(String html) {
    return cleanHtml(html, Whitelist.relaxed().addAttributes("a", "target"));
  }
}
