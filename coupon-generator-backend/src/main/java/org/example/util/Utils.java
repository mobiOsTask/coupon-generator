package org.example.util;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

public class Utils {

    public static String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static String getUniqueID() {
        return Utils.generateRandomPassword(25).toUpperCase() + createRef();
    }

    private static AtomicLong idCounter = new AtomicLong(System.currentTimeMillis());
    public static String createRef() {
        return String.valueOf(idCounter.getAndIncrement());
    }

    public static String getCustomerUniqueId(Long id) {
        return "C" + String.format("%08d", id);
    }

    public static Date getDateStart(Date dateFrom) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date setPaymentDate(Date dateFrom) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            cal.set(Calendar.DAY_OF_MONTH, 27);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date getCurMonth(Date dateFrom) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);

            cal.set(Calendar.DAY_OF_MONTH, 01);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date getDateEnd(Date dateFrom) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;

    }

    public static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String formatDate(Date date,String pattern) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String formatTime(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static String formatDateOnly(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static Date substractDate(Date date, int days) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);
            return cal.getTime();
        }
        return null;
    }

    public static String formatYearAndMonthOnly(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static void main(String[] args) {
        Date date = formatDateOnly("8/9/23");
        System.out.println(date);
    }

    public static Date formatDateOnly(String date) {
        Date parse = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            parse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            try {
                simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                parse = simpleDateFormat.parse(date);
            } catch (ParseException e1) {
            }
        }
        return parse;
    }

    public static Date formatDateAndTime(String date) {
        Date parse = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            parse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static String formatDay(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.format(date);
        }
        return null;
    }

    public static Integer getToday() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return Integer.valueOf(simpleDateFormat.format(new Date()));

    }

    public static String getDayName(Date dateFrom) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        return simpleDateFormat.format(dateFrom).toUpperCase(Locale.ROOT);
    }

    public static String getDayNameForJapanese(Date dateFrom) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.JAPANESE);
        return simpleDateFormat.format(dateFrom);
    }

    public static Date getCurDateTime(String timeZone) {
        return java.sql.Timestamp.valueOf(LocalDateTime.now(ZoneId.of(timeZone)));
    }

    public static Date createDateAndTime(Date date, Date time) {

        Calendar cal = Calendar.getInstance();
        Calendar calTime = Calendar.getInstance();
        cal.setTime(date);
        calTime.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println(cal.getTime());
        System.out.println(cal.getTimeInMillis());
        return cal.getTime();
    }

    public static Date filterTime(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println(cal.getTime());
        System.out.println(cal.getTimeInMillis());
        return cal.getTime();
    }

    public static Date changeDate(String dateFrom) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            String s = dateFrom.split(":")[0];
            String s1 = dateFrom.split(":")[1];
            cal.set(Calendar.YEAR, 1970);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s));
            cal.set(Calendar.MINUTE, Integer.parseInt(s1));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            System.out.println(cal.getTime());
            System.out.println(cal.getTimeInMillis());
            return cal.getTime();
        }
        return null;
    }

    public static Date getDate(Date dateFrom) {
        if (dateFrom != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date addTime(Date date, int min) {

        long timeInSecs = date.getTime();
        return new Date(timeInSecs + (min * 60 * 1000));
    }

    public static Date getDateUsingYearAndMonth(int year, int month, int i) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        if (i == 1) {
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 00);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
        }

        return calendar.getTime();
    }

    public static String getUniqueSMSID() {
        return Utils.generateRandomPassword(25).toUpperCase() + createRef();
    }
}
