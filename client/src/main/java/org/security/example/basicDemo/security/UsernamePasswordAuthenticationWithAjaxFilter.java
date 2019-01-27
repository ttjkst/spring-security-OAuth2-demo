package org.security.example.basicDemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by ttjkst on 2018/9/2.
 */
public class UsernamePasswordAuthenticationWithAjaxFilter extends UsernamePasswordAuthenticationFilter {


    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public UsernamePasswordAuthenticationWithAjaxFilter() {
        super();
    }


    protected String obtainPassword(HttpServletRequest request) {
        if(request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            Object password = request.getAttribute(this.getPasswordParameter());
            request.removeAttribute(this.getPasswordParameter());
            return password==null?null:password.toString();
        }else {
            return   this._obtainPassword(request);
        }
    }

    protected String obtainUsername(HttpServletRequest request) {
        if(request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            Map<String, Object> paramter = getParamter(request);
            Object password = paramter.get(this.getPasswordParameter());
            Object username = paramter.get(this.getUsernameParameter());
            request.setAttribute(this.getPasswordParameter(),password);
            return username==null?null:username.toString();
        }else {
            return   this._obtainUsername(request);
        }
    }

    protected String _obtainPassword(HttpServletRequest request) {
        return request.getParameter(super.getPasswordParameter());
    }

    protected String _obtainUsername(HttpServletRequest request) {
        return request.getParameter(super.getUsernameParameter());
    }

    private Map<String,Object> getParamter(HttpServletRequest request) {
        try {
            return  objectMapper.readValue(request.getInputStream(),Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
