package org.security.example.basicDemo.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login/end")
@RestController
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "/success",method = RequestMethod.GET)
    public Object success(){
        return "login success"+ SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

