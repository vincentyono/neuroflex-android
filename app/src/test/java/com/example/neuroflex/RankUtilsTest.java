package com.example.neuroflex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RankUtilsTest {
    @Test
    public void getTierShouldOutputBeginner1000() {
        assertEquals("Beginner", RankUtils.getTier(1000));
    }

    @Test
    public void getTierShouldOutputBeginner500() {
        assertEquals("Beginner", RankUtils.getTier(500));
    }

    @Test
    public void getTierShouldOutputIntermediate3000() {
        assertEquals("Intermediate", RankUtils.getTier(3000));
    }

    @Test
    public void getTierShouldOutputIntermediate5500() {
        assertEquals("Intermediate", RankUtils.getTier(3000));
    }

    @Test
    public void getTierShouldOutputExpert9000() {
        assertEquals("Expert", RankUtils.getTier(9000));
    }

    @Test
    public void getTierShouldOutputPlatinum17500() {
        assertEquals("Platinum", RankUtils.getTier(17500));
    }

    @Test
    public void getTierShouldOutputPlatinum25000() {
        assertEquals("Platinum", RankUtils.getTier(25000));
    }

    @Test
    public void getTierShouldOutputMaster50000() {
        assertEquals("Master", RankUtils.getTier(50000));
    }

    @Test
    public void getTierShouldOutputGrandMaster100000() {
        assertEquals("Grandmaster", RankUtils.getTier(100000));
    }

    @Test
    public void getTierShouldOutputNeuroflexerAbove100000() {
        assertEquals("Neuroflexer", RankUtils.getTier(9000000));
    }

    @Test
    public void pointsToNextTierShouldOutput1000() {
        assertEquals(1000, RankUtils.pointsToNextTier(2001));
    }

    @Test
    public void pointsToNextTierShouldOutput2000() {
        assertEquals(2000, RankUtils.pointsToNextTier(7001));
    }

    @Test
    public void pointsToNextTierShouldOutput3000() {
        assertEquals(3000, RankUtils.pointsToNextTier(22001));
    }

    @Test
    public void pointsToNextTierShouldOutput4000() {
        assertEquals(4000, RankUtils.pointsToNextTier(46001));
    }

    @Test
    public void pointsToNextTierShouldOutput5000() {
        assertEquals(5000, RankUtils.pointsToNextTier(95001));
    }

    @Test
    public void pointsToNextTierShouldOutput6000() {
        assertEquals(6000, RankUtils.pointsToNextTier(19001));
    }

    @Test
    public void pointsToNextTierShouldOutput7000() {
        assertEquals(7000, RankUtils.pointsToNextTier(43001));
    }

    @Test
    public void pointsToNextTierShouldOutput8000() {
        assertEquals(8000, RankUtils.pointsToNextTier(17001));
    }

    @Test
    public void pointToNextTierShouldOutput0() {
        assertEquals(0, RankUtils.pointsToNextTier(10000123));
    }
}
