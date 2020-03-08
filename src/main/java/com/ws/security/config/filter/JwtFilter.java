package com.ws.security.config.filter;

import com.ws.security.jwt.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * @Description:    //TODO token校验的过滤器
 * @Author:         john
 * @CreateDate:     2020/3/8 17:13
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 17:13
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */

public class JwtFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.header:Authorization}")
    private String authorization;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String jwtToken = req.getHeader(authorization);
        System.out.println(jwtToken);

        filterChain.doFilter(req,response);
    }
}
