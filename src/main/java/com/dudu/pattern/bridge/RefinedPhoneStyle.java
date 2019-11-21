package com.dudu.pattern.bridge;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public abstract class RefinedPhoneStyle extends PhoneStyle{

    public RefinedPhoneStyle(Phone phone) {
        super(phone);
    }

    public void checkPhoneQuality(){
        System.out.println(String.format("%s 质量校验通过 ", phone.getClass().getSimpleName()));
    }
}
