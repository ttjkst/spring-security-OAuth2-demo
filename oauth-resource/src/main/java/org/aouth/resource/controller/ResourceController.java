package org.aouth.resource.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resource")
public class ResourceController implements ApplicationContextAware {
    private static final Log logger = LogFactory.getLog(ResourceController.class);

    @RequestMapping(value = "/get",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String,Object> get(){
        Map<String,Object> resource = new HashMap<>(1);
        resource.put("message","Hello Spring security OAuth2!");
        return resource;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JwtDecoder bean = applicationContext.getBean(JwtDecoder.class);
        logger.debug("JWTDecoder is "+bean.getClass().getName());

    }
}
