package framework.test;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author andy.an
 * @since 2018/12/6
 */
public class Java8DateTest {

    @Test
    public void testLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(localDateTime.format(dateTimeFormatter));

        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(localDate.format(dateTimeFormatter));

        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(localTime.format(dateTimeFormatter));
    }

    @Test
    public void testInstant() {
        Instant instant = Instant.now();
        System.out.println(instant);
    }
}
