package com.est.runtime.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPageController {

    @RequestMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }
}