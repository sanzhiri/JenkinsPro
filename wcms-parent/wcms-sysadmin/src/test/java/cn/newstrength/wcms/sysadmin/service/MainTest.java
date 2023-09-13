/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import org.apache.commons.lang3.RegExUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 日常测试类定义
 * <p>日常测试类定义</p>
 *
 * @author kyrie 2020/12/7 10:53 上午
 * @since jdk1.8
 */
public class MainTest {

    @Test
    public void testPattern(){
        String pattern = "^(?![_]{2})[a-zA-Z_]+$";
        String content = "_wangrui_wabg___abcd";
        Assert.assertTrue(Pattern.matches(pattern, content));
        System.out.println("正则匹配成功");
    }

    @Test
    public void testDate(){
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        System.out.println(date);

        Date date1 = Date.from(LocalDate.parse("2099-01-01").atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        System.out.println(date1);
    }

    @Test
    public void testList(){
        List<String> lists = new ArrayList<>();
        lists.add("A");
        lists.add("B");
        lists.add("C");
        lists.add("D");


        lists.forEach(System.out::println);
    }

    @Test
    public void testReg (){
        String reg = "/CMSResourceUrl";
        String str = "http://localhost:7070/resource";
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title>test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div>\n" +
                "\t\t<img src=\"/CMSResourceUrl/2021/04/05/xxddxxdd.jpg\">\n" +
                "\t\t<a href=\"/CMSResourceUrl/2021/04/05/testpdf.pdf\"></a>\n" +
                "\t\t<p>这是一段文章的部分内容呀哈哈哈哈</p>\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>";
        System.out.println(RegExUtils.replaceAll(html, reg, str));
    }

}
