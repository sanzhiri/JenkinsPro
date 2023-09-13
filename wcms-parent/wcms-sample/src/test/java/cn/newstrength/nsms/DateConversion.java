package cn.newstrength.nsms;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateConversion {
    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式化器，格式化为"yyyyMM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        // 将日期进行转化为指定格式
        String tableDate = currentDate.format(formatter);
        // 解析日期字符串
        YearMonth yearMonth = YearMonth.parse(tableDate, formatter);
        YearMonth Minus1 = yearMonth.minusMonths(1);//前一个月
        String MonthMinus1 = Minus1.format(formatter);
        YearMonth Minus2 = yearMonth.minusMonths(2);//前两个月
        String MonthMinus2 = Minus2.format(formatter);
        YearMonth Minus3 = yearMonth.minusMonths(3);
        String MonthMinus3 = Minus3.format(formatter);
        YearMonth Minus4 = yearMonth.minusMonths(4);
        String MonthMinus4 = Minus4.format(formatter);
        YearMonth Minus5 = yearMonth.minusMonths(5);
        String MonthMinus5 = Minus5.format(formatter);
        System.out.println(MonthMinus1);
        System.out.println(MonthMinus2);
        System.out.println(MonthMinus3);
        System.out.println(MonthMinus4);
        System.out.println(MonthMinus5);
        System.out.println(tableDate);
    }
}