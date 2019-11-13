package com.dudu.pattern.Bridge.model;

import com.dudu.pattern.Bridge.PhoneModel;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public class MaxModel implements PhoneModel {

    @Override
    public void productionModel(int count) {
        System.out.println("确定为max型号手机, 个数: " + count);
    }
}
