package com.dudu.pattern.bridge.brand;

import com.dudu.pattern.bridge.Phone;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public class HuaweiPhone implements Phone {

    @Override
    public void makePhone(int count) {
        System.out.println("制作%s台苹果手机");
    }
}
