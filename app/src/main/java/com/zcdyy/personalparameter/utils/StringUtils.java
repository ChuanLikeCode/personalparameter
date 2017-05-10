package com.zcdyy.personalparameter.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class StringUtils {


    public static String join(Collection c, String ch) {
        if (c == null)
            return null;
        if (c.size() == 0) {
            return "";
        }
        Iterator it = c.iterator();
        StringBuffer sb = new StringBuffer();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(ch).append(it.next());
        }
        return sb.toString();
    }

    /**
     * @param val
     * @return
     */
    public static boolean isNotEmpty(String val) {
        return val != null && !val.equals("") && !val.trim().equals("");
    }

    public static String createNumber(String code, String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
        Random r = new Random();
        Double d = r.nextDouble() * 97;
        String billPayNumber = code + id + sdf.format(new Date())
                + d.toString().substring(3, 7);
        return billPayNumber;
    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.equals(""));
    }

    /**
     * 字符串是否一样（不包括同为null或空的情况）
     */
    public static boolean equals(String str1, String str2) {
        return (!isEmpty(str1) && !isEmpty(str2) && str1.equals(str2));
    }

    /**
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else {
            return mobiles.matches(telRegex);
        }
    }

}
