package com.example.spring_boot_esercizio.middleware;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class EntityRequestFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                
        CachedBodyHttpServletRequest requestWrapper = new CachedBodyHttpServletRequest((HttpServletRequest) request);
        String body = requestWrapper.getRequestBody();
        
        System.out.println("Applying filter...");
        // Wrapping a single object in an array
        if(body.startsWith("{")) {
            requestWrapper.setRequestBody("["+body+"]");
        }
        chain.doFilter((ServletRequest) requestWrapper, response);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
