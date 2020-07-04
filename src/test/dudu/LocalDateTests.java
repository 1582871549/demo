import com.dudu.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@ActiveProfiles("dev")
public class LocalDateTests {

    private static final Logger log = LoggerFactory.getLogger(LocalDateTests.class);

    // 只会获取年月日
    @Test
    public void getLocalDateTest() {

        // 获取当前年月日
        LocalDate localDate = LocalDate.now();

        // 构造指定的年月日
        LocalDate localDate1 = LocalDate.of(2019, 9, 10);

        // 获取年、月、日、星期几
        int year = localDate.getYear();
        int year1 = localDate.get(ChronoField.YEAR);

        Month month = localDate.getMonth();
        int month1 = localDate.get(ChronoField.MONTH_OF_YEAR);

        int day = localDate.getDayOfMonth();
        int day1 = localDate.get(ChronoField.DAY_OF_MONTH);

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeek1 = localDate.get(ChronoField.DAY_OF_WEEK);

        System.out.println("---------------");
        System.out.println(year);
    }


    // 只会获取几点几分几秒
    @Test
    public void getLocalTimeTest() {
        LocalTime localTime = LocalTime.of(13, 51, 10);
        LocalTime localTime1 = LocalTime.now();

        // 获取时分秒

        //获取小时
        int hour = localTime.getHour();
        int hour1 = localTime.get(ChronoField.HOUR_OF_DAY);
        //获取分
        int minute = localTime.getMinute();
        int minute1 = localTime.get(ChronoField.MINUTE_OF_HOUR);
        //获取秒
        int second = localTime.getSecond();
        int second1 = localTime.get(ChronoField.SECOND_OF_MINUTE);
    }

    // 获取年月日时分秒，等于LocalDate+LocalTime
    @Test
    public void getLocalDateTimeTest() {

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();

        LocalDateTime localDateTime1 = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);

        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        LocalDateTime localDateTime3 = localDate.atTime(localTime);
        LocalDateTime localDateTime4 = localTime.atDate(localDate);

        // 获取LocalDate
        LocalDate localDate2 = localDateTime.toLocalDate();

        // 获取LocalTime
        LocalTime localTime2 = localDateTime.toLocalTime();
    }

    // 获取秒数
    @Test
    public void getInstantTest() {
        // 创建Instant对象
        Instant instant = Instant.now();

        // 获取秒数
        long currentSecond = instant.getEpochSecond();

        // 获取毫秒数
        long currentMilli = instant.toEpochMilli();

        // 个人觉得如果只是为了获取秒数或者毫秒数，使用System.currentTimeMillis()来得更为方便
    }

    /**
     * 修改LocalDate、LocalTime、LocalDateTime、Instant
     * LocalDate、LocalTime、LocalDateTime、Instant为不可变对象，修改这些对象对象会返回一个副本
     * 增加、减少年数、月数、天数等 以LocalDateTime为例
     */
    @Test
    public void updateTimeTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);

        // 增加一年
        localDateTime = localDateTime.plusYears(1);
        localDateTime = localDateTime.plus(1, ChronoUnit.YEARS);

        // 减少一个月
        localDateTime = localDateTime.minusMonths(1);
        localDateTime = localDateTime.minus(1, ChronoUnit.MONTHS);

        // 通过with修改某些值

        // 修改年为2019
        localDateTime = localDateTime.withYear(2020);

        // 修改为2022
        localDateTime = localDateTime.with(ChronoField.YEAR, 2022);
    }

    // 时间计算
    // 比如有些时候想知道这个月的最后一天是几号、下个周末是几号，通过提供的时间和日期API可以很快得到答案
    @Test
    public void timeCalculationTest() {
        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 格式化时间
     * DateTimeFormatter默认提供了多种格式化方式，如果默认提供的不能满足要求，
     * 可以通过DateTimeFormatter的ofPattern方法创建自定义格式化方式
     */
    @Test
    public void formatTimeTest() {
        LocalDate localDate = LocalDate.of(2019, 9, 10);
        String s1 = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 自定义格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String s3 = localDate.format(dateTimeFormatter);
    }

    // 解析时间, 和SimpleDateFormat相比，DateTimeFormatter是线程安全的
    @Test
    public void analysisTimeTest() {
        LocalDate localDate1 = LocalDate.parse("20190910", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate localDate2 = LocalDate.parse("2019-09-10", DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Test
    public void aa() {

        String day1 = LocalDate.now().plusDays(-3).toString();
        String day2 = LocalDateTime.now().plusDays(-3).toString();

        System.out.println("===============   " + day1);
        System.out.println("===============   " + day2);
    }

}
