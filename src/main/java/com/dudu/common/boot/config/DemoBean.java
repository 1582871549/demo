package com.dudu.common.boot.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据不同的环境来注入该bean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoBean {
    private String content;
}
