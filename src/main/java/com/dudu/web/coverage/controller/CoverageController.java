package com.dudu.web.coverage.controller;

import com.dudu.service.coverage.CoverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/23
 * @since 1.0.0
 */
@RestController
@RequestMapping("/coverage")
public class CoverageController {

    @Autowired
    private CoverageService coverageService;

}
