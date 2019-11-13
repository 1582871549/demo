package com.dudu.pattern.Bridge;

/**
 * @author mengli
 * @create 2019/11/12
 * @since 1.0.0
 */
public abstract class RefinedPhoneBrand extends PhoneBrand{

    public RefinedPhoneBrand(PhoneModel phoneModel) {
        super(phoneModel);
    }

    public void checkPhoneQuality(){
        System.out.println(String.format("%s 质量校验通过 ", phoneModel.getClass().getSimpleName()));
    }
}
