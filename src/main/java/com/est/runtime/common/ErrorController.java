package com.est.runtime.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/denied")
    public String deniedPage() {
        return "denied";
    }
}