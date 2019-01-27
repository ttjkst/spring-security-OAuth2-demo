package org.aouth.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resource")
public class ResourceController {


    @RequestMapping(value = "/get",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String,Object> get(){
        Map<String,Object> resource = new HashMap<>(1);
        resource.put("message","Hello Spring security OAuth2!");
        return resource;
    }

}
