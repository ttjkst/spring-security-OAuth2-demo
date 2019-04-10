package org.oauth.authoriaztion.authority;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

public class AnthMacthTest {

    @Test
    public void anthMacthTest(){
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/user/{name}");
        MockHttpServletRequest httpServletRequest = MockMvcRequestBuilders.get("/user/sate/sas").buildRequest(null);

        boolean matches = matcher.matches(httpServletRequest);
        Map<String, String> stringStringMap = matcher.extractUriTemplateVariables(httpServletRequest);
        Assert.assertTrue(matches);
    }
}
