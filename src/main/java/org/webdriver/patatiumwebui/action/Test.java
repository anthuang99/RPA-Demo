package org.webdriver.patatiumwebui.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    private static void test1(String[] args) throws Exception {
        String text = "本月纳税申报截止日期是2018-01-01,本月增值税申报截止日期是2018-11-15,本月消费税申报截止日期是2018-11-15";
        Pattern pattern = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String yyyyMMdd = matcher.group(0);
            String[] array = yyyyMMdd.split("-");
            System.out.println(array[0]);
            System.out.println(Integer.valueOf(array[1]));
            System.out.println(Integer.valueOf(array[2]));
            String month = array[1];
            if (month.startsWith("0")) {
                month = month.substring(1);
            }
            System.out.println("month is:" + month + ", array1:" + array[1]);
            System.out.println(yyyyMMdd);
        }
    }

    private static void test2() {
        String osName = System.getProperty("os.name").toLowerCase();
        boolean result = osName.indexOf("windows 7") > -1;
        System.out.println(osName);
        System.out.println(result);
    }



//    System.getProperty("os.name").toLowerCase();
    public static void main(String[] args) throws Exception {
        test2();
    }


}
