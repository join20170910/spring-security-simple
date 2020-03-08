package com.ws.security.config.filter;

import com.ws.security.jwt.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @Description:    //TODO
 * @Author:         john
 * @CreateDate:     2020/3/8 21:19
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 21:19
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    private String authorization;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,String authorization) {

        super(authenticationManager);
        this.authorization = authorization;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String jwtToken = req.getHeader(authorization);
        System.out.println(jwtToken);
        // 如果请求头中没有Authorization信息则直接放行了
        if (jwtToken == null || !jwtToken.startsWith(authorization)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(jwtToken));

        super.doFilterInternal(request, response, chain);
    }

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    // 这里从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(authorization, "");
        String username = jwtTokenUtils.getUsername(token);
        if (username != null){
            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        }
        return null;
    }
}
