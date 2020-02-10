package zy.pointer.j2easy.framework.commons;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static Date localDateTimeToDate( LocalDateTime localDateTime ){
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static void main(String[] args) {

        System.out.println( localDateTimeToDate( LocalDateTime.now() ) );

    }

}
