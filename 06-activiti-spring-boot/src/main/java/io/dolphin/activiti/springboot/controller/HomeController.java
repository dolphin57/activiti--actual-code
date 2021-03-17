package io.dolphin.activiti.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-7 16:16
 */
@RestController
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "hello world";
    }
}
