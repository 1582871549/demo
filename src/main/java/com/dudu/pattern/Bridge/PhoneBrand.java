package com.dudu.pattern.Bridge;

/**
 * 品牌
 *
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public abstract class PhoneBrand {

    protected PhoneModel phoneModel;

    public PhoneBrand(PhoneModel phoneModel){
        this.phoneModel = phoneModel;
    }

    public abstract void orderPhone(int count);
}
