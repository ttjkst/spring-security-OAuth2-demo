package org.security.example.basicDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Basic {

    @GetMapping("/login")
    public ModelAndView toLogin(){
        return new ModelAndView("/login");
    }
}
