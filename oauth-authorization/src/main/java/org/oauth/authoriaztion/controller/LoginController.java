package org.oauth.authoriaztion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {


    @RequestMapping(value = "/fail",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String loginFailProcess(){
        return "fail";
    }

}
