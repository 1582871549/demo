package com.dudu.pattern.Bridge.brand;

import com.dudu.pattern.Bridge.PhoneModel;
import com.dudu.pattern.Bridge.RefinedPhoneBrand;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public class ApplePhone extends RefinedPhoneBrand {

    public ApplePhone(PhoneModel phoneModel) {
        super(phoneModel);
    }

    @Override
    public void orderPhone(int count) {
        phoneModel.productionModel(count);
        System.out.println("正在生产苹果" + phoneModel.getClass().getSimpleName() + "手机, 个数: " + count);
    }
}
