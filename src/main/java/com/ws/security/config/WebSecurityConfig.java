package com.ws.security.config;

import com.ws.security.config.filter.JWTAuthenticationFilter;
import com.ws.security.config.filter.JWTAuthorizationFilter;
import com.ws.security.config.handler.*;
import com.ws.security.config.service.UserDetailsServiceImpl;
import com.ws.security.jwt.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @Author: Hutengfei
 * @Description:
 * @Date Create in 2019/8/28 20:15
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.header:Authorization}")
    private String authorization;

    @Value("${jwt.tokenHead:Bearer}")
    private String tokenHead;

    //登录成功处理逻辑
    @Autowired
    CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;

    //登录失败处理逻辑
    @Autowired
    CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    //权限拒绝处理逻辑
    @Autowired
    CustomizeAccessDeniedHandler accessDeniedHandler;

    //匿名用户访问无权限资源时的异常
    @Autowired
    CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    //会话失效(账号被挤下线)处理逻辑
    @Autowired
    CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    //登出成功处理逻辑
    @Autowired
    CustomizeLogoutSuccessHandler logoutSuccessHandler;

    //访问决策管理器
    @Autowired
    CustomizeAccessDecisionManager accessDecisionManager;

    //实现权限拦截
    @Autowired
    CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        //获取用户账号密码及权限信息
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Qualifier("jwtTokenUtils")
    public JwtTokenUtils jwtTokenUtils(){
        return new JwtTokenUtils();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests().
                //antMatchers("/getUser").hasAuthority("query_user").
                //antMatchers("/**").fullyAuthenticated().
                withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(accessDecisionManager);//决策管理器
                        o.setSecurityMetadataSource(securityMetadataSource);//安全元数据源
                        return o;
                    }
                }).
                //登出
                and().logout().
                    permitAll().//允许所有用户
                    logoutSuccessHandler(logoutSuccessHandler).//登出成功处理逻辑
                    deleteCookies("JSESSIONID").//登出之后删除cookie
                //登入
                and().formLogin().
                    permitAll().//允许所有用户
                    successHandler(authenticationSuccessHandler).//登录成功处理逻辑
                    failureHandler(authenticationFailureHandler).//登录失败处理逻辑
                //异常处理(权限拒绝、登录失效等)
                and().exceptionHandling().
                    accessDeniedHandler(accessDeniedHandler).//权限拒绝处理逻辑
                    authenticationEntryPoint(authenticationEntryPoint).//匿名用户访问无权限资源时的异常处理
                //会话管理
                and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtTokenUtils(),tokenHead,authorization))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(),authorization))
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);;
    }
}
