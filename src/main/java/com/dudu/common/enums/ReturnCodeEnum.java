package com.dudu.common.enums;

/**
 * @author 大橙子
 * 定义系统常用状态值
 */
public enum ReturnCodeEnum {

    BASE_SUCCESS("10001", "成功"),
    BASE_ERROR("10002", "失败"),
    SYSTEM_ERROR("10099", "系统错误");

    private String code;
    private String name;

    private ReturnCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {

        for (ReturnCodeEnum enuma : values()) {
            if (enuma.getCode().equals(code)) {
                return enuma.getName();
            }
        }
        return "未知枚举项";
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
