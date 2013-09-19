package com.au.post;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class AustraliaPostDateTest {

    private static final String START_DATE = "2013-09-13 19";
    private static final String END_DATE = "2013-09-15";

    @Test
    public void shouldCreateDateModel() {
        new AustraliaPostDate(START_DATE, END_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenStartDateAfterEndDate() {
        new AustraliaPostDate(END_DATE, START_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenStartDateNotSpecified() {
        new AustraliaPostDate(null, END_DATE);
    }

    @Test
    public void shouldCalculateDaysBetweenWithTruncatedDateAndInclusive() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate("2013-09-1 01", "2013-09-15 19");
        assertThat(australiaPostDate.getDayBetween(), is(15l));
    }

    @Test
    public void shouldCalculateDaysBetweenForSameDayInclusive() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate("2013-09-1 01", "2013-09-1 19");
        assertThat(australiaPostDate.getDayBetween(), is(1l));
    }

    @Test
    public void shouldCalculateMonthsBetweenForSameDay() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate("2013-09-11 01", "2013-09-11 19");
        assertThat(australiaPostDate.getMonthBetween(), is(0.0));
    }


    @Test
    public void shouldCalculateMonthBetweenForExactOneMonth() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate("2013-09-13 01", "2013-10-13 19");
        assertThat(australiaPostDate.getMonthBetween(), is(1d));
    }

    @Test
    public void shouldCalculateMonthBetweenForOneAndHalfMonth() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate("2013-09-01 01", "2013-10-15 19");
        assertThat(australiaPostDate.getMonthBetween(), is(1.5));
    }

    @Test
    public void shouldCalculateMonthBetweenForOneAndAThirdMonth() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate("2013-09-01 01", "2013-10-10 19");
        assertThat(australiaPostDate.getMonthBetween(), is(1.3));
    }

    @Test
    public void shouldBetweenRange() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate(START_DATE, END_DATE);
        assertThat(australiaPostDate.isBetween("2013-09-13 19"), is(true));
    }

    @Test
    public void shouldNotBetweenRangeWhenDateBeforeStartDate() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate(START_DATE, END_DATE);
        assertThat(australiaPostDate.isBetween("2013-09-01 19"), is(false));
    }

    @Test
    public void shouldNotBetweenRangeWhenDateAfterEndDate() {
        AustraliaPostDate australiaPostDate = new AustraliaPostDate(START_DATE, END_DATE);
        assertThat(australiaPostDate.isBetween("2013-09-21 19"), is(false));
    }

}
