package com.example.neuroflex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StreakHelperTest {
    @Test
    public void getCurrentDateShouldHaveYYYYMMDDFormat() {
        String date = StreakHelper.getCurrentDate();
        assertTrue(date.split("-")[0].length() == 4);
        assertTrue(date.split("-")[1].length() == 2);
        assertTrue(date.split("-")[2].length() == 2);
    }

    @Test
    public void isConsecutiveDayShouldReturnTrue() {
        assertTrue(StreakHelper.isConsecutiveDay("2024-05-20", "2024-05-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2024-04-25", "2024-04-26"));
        assertTrue(StreakHelper.isConsecutiveDay("2024-07-20", "2024-07-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2024-02-20", "2024-02-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2023-05-20", "2023-05-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2020-03-20", "2020-03-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2021-02-20", "2021-02-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2023-07-20", "2023-07-21"));
        assertTrue(StreakHelper.isConsecutiveDay("2020-09-20", "2020-09-21"));
    }

    @Test
    public void isConsecutiveDayShouldReturnFalse() {
        assertFalse(StreakHelper.isConsecutiveDay("2024-05-20", "2024-05-30"));
        assertFalse(StreakHelper.isConsecutiveDay("2024-04-25", "2025-01-26"));
        assertFalse(StreakHelper.isConsecutiveDay("2024-07-20", "2024-07-31"));
        assertFalse(StreakHelper.isConsecutiveDay("2024-02-20", "2024-02-28"));
        assertFalse(StreakHelper.isConsecutiveDay("2023-05-20", "2023-06-21"));
        assertFalse(StreakHelper.isConsecutiveDay("2020-03-20", "2020-07-21"));
        assertFalse(StreakHelper.isConsecutiveDay("2021-02-20", "2021-02-24"));
        assertFalse(StreakHelper.isConsecutiveDay("2023-07-20", "2023-07-22"));
        assertFalse(StreakHelper.isConsecutiveDay("2020-09-20", "2021-09-21"));
    }
}
