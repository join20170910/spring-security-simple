package com.ws.security.config.handler;

import com.alibaba.fastjson.JSON;
import com.ws.security.common.entity.JsonResult;
import com.ws.security.common.utils.ResultTool;
import com.ws.security.entity.SysUser;
import com.ws.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


/**
 * @Description:    //TODO 登录成功处理逻辑
 * @Author:         john
 * @CreateDate:     2020/3/8 12:20
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 12:20
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    SysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.selectByName(userDetails.getUsername());
        sysUser.setLastLoginTime(new Date());
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUser(sysUser.getId());
        sysUserService.update(sysUser);

        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展

        //返回json数据
        JsonResult result = ResultTool.success();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
