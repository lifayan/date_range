package com.au.post;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AustraliaPostDate {
    private static final DateFormat DATE_FORMAT_WITH_HOUR = new SimpleDateFormat("yyyy-MM-dd HH");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final long MILLISECOND_IN_A_DAY = 1000 * 60 * 60 * 24;
    private static final double AVERANGE_DAYS_IN_A_MONTH = 30.4;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("######0.00");

    static {
        DATE_FORMAT_WITH_HOUR.setLenient(false);
        DATE_FORMAT.setLenient(false);
    }

    private Date startDate;
    private Date endDate;

    public AustraliaPostDate(String startDateStr, String endDateStr) {
        if (startDateStr == null || endDateStr == null) {
            throw new IllegalArgumentException("Neither start date nor end date can be null");
        }

        try {
            startDate = truncateToDate(parseDate(startDateStr));
            endDate = truncateToDate(parseDate(endDateStr));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid format for start date or end date", e);
        }

        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("start date must before end date");
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        Date date = null;
        try {
            date = DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            date = DATE_FORMAT_WITH_HOUR.parse(dateStr);
        }
        return date;
    }

    private Date truncateToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    public long getDayBetween() {
        return calculateDay(startDate, endDate);
    }

    private long calculateDay(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / MILLISECOND_IN_A_DAY + 1;
    }

    private double round(double value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getMonthBetween() {
        double month = 0;
        Calendar calendar_start = Calendar.getInstance();
        calendar_start.setTime(startDate);

        Calendar calendar_end = Calendar.getInstance();
        calendar_end.setTime(endDate);

        calendar_start.add(Calendar.MONTH, 1);

        while (!calendar_start.after(calendar_end)) {
            month = month + 1;
            calendar_start.add(Calendar.MONTH, 1);
        }

        calendar_start.add(Calendar.MONTH, -1);

        if (calendar_start.getTimeInMillis() != calendar_end.getTimeInMillis()) {

            month = month + calculateDay(calendar_start.getTime(), calendar_end.getTime()) / AVERANGE_DAYS_IN_A_MONTH;
        }
        return round(month);
    }

    public boolean isBetween(String dateStr) {
        Date date = null;
        try {
            date = truncateToDate(parseDate(dateStr));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        return date != null && !startDate.after(date) && !endDate.before(date);
    }
}
