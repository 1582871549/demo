package com.dudu.pattern.bridge.style;

import com.dudu.pattern.bridge.Phone;
import com.dudu.pattern.bridge.RefinedPhoneStyle;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public class MaxStyle extends RefinedPhoneStyle {

    public MaxStyle(Phone phone) {
        super(phone);
    }

    @Override
    public void orderPhoneStyle(String versionNumber) {
        System.out.println(String.format("正在生产%s %s max", phone.getClass().getSimpleName(), versionNumber));
    }
}
