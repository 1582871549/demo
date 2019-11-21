package com.dudu.pattern.strategy;

public interface CalculationFare {

    /**
     * 计算各出行方式的金额
     *
     * @param distance 路程
     * @param peopleNumber 人数
     * @return 需要支付金额
     */
    double calculateTrafficFee(int distance, int peopleNumber);
}
