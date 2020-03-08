package com.ws.security.config.filter;

import com.ws.security.entity.SysUser;
import com.ws.security.jwt.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Description:    //TODO  JWT 过滤器
 * @Author:         john
 * @CreateDate:     2020/3/8 16:15
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 16:15
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String tokenHead;
    private String authorization;

    private AuthenticationManager authenticationManager;
    private JwtTokenUtils jwtTokenUtils;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenUtils jwtTokenUtils,String tokenHead,String authorization) {
        this.authenticationManager = authenticationManager;
        this.tokenHead = tokenHead;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authorization = authorization;
        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 从输入流中获取到登录的信息
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        User jwtUser = (User) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
        String token = jwtTokenUtils.createToken(jwtUser.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setHeader(authorization, tokenHead + token);
    }

    // 这是验证失败时候调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
