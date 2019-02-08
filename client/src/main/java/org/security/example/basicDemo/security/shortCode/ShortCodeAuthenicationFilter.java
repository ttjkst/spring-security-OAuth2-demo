package org.security.example.basicDemo.security.shortCode;

import org.security.example.basicDemo.security.shortCode.contraint.ShortCodeContraint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ttjkst on 2018/9/2.
 */
public class ShortCodeAuthenicationFilter extends AbstractAuthenticationProcessingFilter {

    public ShortCodeAuthenicationFilter() {
        super(ShortCodeContraint.DEFALUT_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String uniqueCode  = request.getParameter(ShortCodeContraint.UNIQUE_CODE_PARAMMER);
        String shortCode   = request.getParameter(ShortCodeContraint.SHORT_CODE_PARAMMER);
        ShortCodeAuthenticationToken shortCodeAuthenticationToken = new ShortCodeAuthenticationToken(shortCode,uniqueCode);
        return this.getAuthenticationManager().authenticate(shortCodeAuthenticationToken);
    }
}
