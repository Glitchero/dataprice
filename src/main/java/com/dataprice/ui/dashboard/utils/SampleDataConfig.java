package com.dataprice.ui.dashboard.utils;

import java.util.List;

/**
 * Sample data config holder based on chart.js sample utils
 */
public class SampleDataConfig {

    private double min = 0;
    private double max = 1;
    private List<Double> from;
    private int count = 8;
    private int decimals = 2;
    private int continuity = 1;

    public SampleDataConfig min(double min) {
        this.min = min;
        return this;
    }

    public SampleDataConfig max(double max) {
        this.max = max;
        return this;
    }

    public SampleDataConfig from(List<Double> from) {
        this.from = from;
        return this;
    }

    public SampleDataConfig count(int count) {
        this.count = count;
        return this;
    }

    public SampleDataConfig decimals(int decimals) {
        this.decimals = decimals;
        return this;
    }

    public SampleDataConfig continuity(int continuity) {
        this.continuity = continuity;
        return this;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    public int getDecimals() {
        return decimals;
    }

    public int getContinuity() {
        return continuity;
    }

    public List<Double> getFrom() {
        return from;
    }
}