package com.dudu.pattern.strategy;

/**
 * @author mengli
 * @create 2019/11/14
 * @since 1.0.0
 */
public class StationContext {

    public double goToThePark(CalculationFare calculationFare, int distance, int peopleNumber) {
        return calculationFare.calculateTrafficFee(distance, peopleNumber);
    }
}
