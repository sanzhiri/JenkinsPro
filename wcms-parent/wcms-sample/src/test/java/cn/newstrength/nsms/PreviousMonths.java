package cn.newstrength.nsms;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PreviousMonths {
    public static void main(String[] args) {
        // 原始日期字符串
        String dateString = "202309";

        // 定义日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        // 解析日期字符串
        YearMonth yearMonth = YearMonth.parse(dateString, formatter);

        // 获取前五个月的日期
        for (int i = 1; i <= 5; i++) {
            YearMonth previousMonth = yearMonth.minusMonths(i);
            String previousMonthString = previousMonth.format(formatter);
            System.out.println("前第 " + i + " 个月的日期: " + previousMonthString);
        }
    }
}