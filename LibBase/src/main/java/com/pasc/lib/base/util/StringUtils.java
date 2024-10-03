package com.pasc.lib.base.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yintangwen952 on 2018/9/2.
 */
public class StringUtils {
    public static final String Kilometer = "\u516c\u91cc";// "公里";
    public static final String Meter = "\u7c73";// "米";
    public static final String ByFoot = "\u6b65\u884c";// "步行";
    public static final String To = "\u53bb\u5f80";// "去往";
    public static final String Station = "\u8f66\u7ad9";// "车站";
    public static final String TargetPlace = "\u76ee\u7684\u5730";// "目的地";
    public static final String StartPlace = "\u51fa\u53d1\u5730";// "出发地";
    public static final String About = "\u5927\u7ea6";// "大约";
    public static final String Direction = "\u65b9\u5411";// "方向";

    public static final String GetOn = "\u4e0a\u8f66";// "上车";
    public static final String GetOff = "\u4e0b\u8f66";// "下车";
    public static final String Zhan = "\u7ad9";// "站";

    public static final String cross = "\u4ea4\u53c9\u8def\u53e3"; // 交叉路口
    public static final String type = "\u7c7b\u522b"; // 类别
    public static final String address = "\u5730\u5740"; // 地址
    public static final String PrevStep = "\u4e0a\u4e00\u6b65";
    public static final String NextStep = "\u4e0b\u4e00\u6b65";
    public static final String Gong = "\u516c\u4ea4";
    public static final String ByBus = "\u4e58\u8f66";
    public static final String Arrive = "\u5230\u8FBE";// 到达

    /**
     * @param content  传入的字符串
     * @param frontNum 保留前面字符位数
     * @param endNum   保留后面字符位数
     * @return
     */
    public static String getStarString(String content, int frontNum, int endNum) {

        if (frontNum >= content.length() || frontNum < 0) {
            return content;
        }
        if (endNum >= content.length() || endNum < 0) {
            return content;
        }
        if (frontNum + endNum >= content.length()) {
            return content;
        }
        String starStr = "";
        for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, frontNum) + starStr
                + content.substring(content.length() - endNum, content.length());

    }


    /**
     * 文本关键词变色
     *
     * @param text         文本
     * @param keyWord      关键字
     * @param keyWordColor 关键字颜色
     * @return
     */
    public static SpannableStringBuilder setKeyWordTextColor(String text, String keyWord, int keyWordColor) {
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = keyWord.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(keyWord);

            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        } while (start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(
                    new ForegroundColorSpan(keyWordColor),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return styledText;
    }


    /**
     * 去掉String字符串开头和结尾的空格
     *
     * @param str 字符串
     */
    public static String trimStartAndEnd(String str) {
        String formatStr = str.trim();
        if (TextUtils.isEmpty(formatStr)) {
            return formatStr;
        }
        final char[] value = formatStr.toCharArray();
        int start = 0;
        int end = formatStr.length() - 1;
        while (start <= end && value[start] == ' ') {
            start++;
        }
        if (start == 0) {
            return formatStr;
        }
        if (start >= end) {
            return "";
        }
        return formatStr.substring(start, end);
    }

    public static String cleanString(String aString) {
        if (aString == null) return null;
        String cleanString = "";
        for (int i = 0; i < aString.length(); ++i) {
            cleanString += cleanChar(aString.charAt(i));
        }
        return cleanString;
    }

    private static char cleanChar(char aChar) {

        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) return (char) i;
        }

        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) return (char) i;
        }

        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) return (char) i;
        }

        // other valid characters
        switch (aChar) {
            case '/':
                return '/';
            case '.':
                return '.';
            case '-':
                return '-';
            case '_':
                return '_';
            case ' ':
                return ' ';
        }
        return '%';
    }
}
