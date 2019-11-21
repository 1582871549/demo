package com.dudu.pattern.strategy.impl;

import com.dudu.pattern.strategy.CalculationFare;

/**
 * @author mengli
 * @create 2019/11/14
 * @since 1.0.0
 */
public class Taxi implements CalculationFare {

    @Override
    public double calculateTrafficFee(int distance, int peopleNumber) {
        return distance < 5 ? 5 : 5 + ((distance - 5)*2);
    }
}
