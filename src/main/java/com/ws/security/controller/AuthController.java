package com.ws.security.controller;

import com.ws.security.common.entity.JsonResult;
import com.ws.security.common.utils.ResultTool;
import com.ws.security.entity.SysUser;
import com.ws.security.jwt.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author john
 */
@RestController

public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @PostMapping("/auth")
    public JsonResult login(@RequestBody SysUser sysUser, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(sysUser.getUserName());
        String token = jwtTokenUtils.createToken(userDetails.getUsername(),true);
        return ResultTool.success(token);
    }
}
