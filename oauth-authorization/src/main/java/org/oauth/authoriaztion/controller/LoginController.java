package org.oauth.authoriaztion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {


    @RequestMapping(value = "/fail",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String loginFailProcess(){
        return "fail";
    }

    @RequestMapping(value = "/page")
    public ModelAndView loginPage(){
        return new ModelAndView("/authorizan");
    }

}
