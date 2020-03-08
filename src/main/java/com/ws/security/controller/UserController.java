package com.ws.security.controller;

import com.ws.security.common.entity.JsonResult;
import com.ws.security.common.utils.ResultTool;
import com.ws.security.entity.SysUser;
import com.ws.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Hutengfei
 * @Description:
 * @Date Create in 2019/8/28 19:34
 */
@RestController
public class UserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/getUser")
    public JsonResult getUser() {
        List<SysUser> users = sysUserService.queryAllByLimit(1, 100);
        return ResultTool.success(users);
    }
    @GetMapping("/test")
    public JsonResult test() {
        return ResultTool.success("hello world");
    }
    @PostMapping("/register")
    public JsonResult registerUser(@RequestBody Map<String,String> registerUser){
        SysUser user = new SysUser();
        user.setUsername(registerUser.get("username"));
        // 记得注册的时候把密码加密一下
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));

        SysUser sysUser = sysUserService.insert(user);
        return ResultTool.success(sysUser);
    }
}
