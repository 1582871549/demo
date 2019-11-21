package com.dudu.pattern.builder;

import lombok.*;

/**
 * @author mengli
 * @create 2019/11/15
 * @since 1.0.0
 */
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Computer {

    private final String CPU;           // cpu
    private final String videoCard;     // 显卡
    private final String processor;     // 处理器
    private final boolean SSD;           // 固态硬盘

}
