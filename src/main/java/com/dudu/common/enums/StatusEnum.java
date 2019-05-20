package com.dudu.common.enums;

public enum StatusEnum {

    SUCCESS(1, "成功"),
    ERROR(0, "失败");

    private int code;
    private String name;

    private StatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {

        for (StatusEnum enuma : values()) {
            if (enuma.getCode() == code) {
                return enuma.getName();
            }
        }
        return "未知枚举项";
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
