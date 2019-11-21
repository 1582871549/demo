package com.dudu.pattern.bridge;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public abstract class PhoneStyle {

    protected Phone phone;

    public PhoneStyle(Phone phone) {
        this.phone = phone;
    }

    public abstract void orderPhoneStyle(String versionNumber);
}
