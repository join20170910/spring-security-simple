package com.ws.security.config.service;

import com.ws.security.entity.SysPermission;
import com.ws.security.entity.SysUser;
import com.ws.security.service.SysPermissionService;
import com.ws.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description:    //TODO 用户登录认证逻辑
 * @Author:         john
 * @CreateDate:     2020/3/8 12:24
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 12:24
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }
        //根据用户名查询用户
        SysUser sysUser = sysUserService.selectByName(username);
        if (sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (sysUser != null) {
            //获取该用户所拥有的权限
            List<SysPermission> sysPermissions = sysPermissionService.selectListByUser(sysUser.getId());
            // 声明用户授权
            sysPermissions.forEach(sysPermission -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
                grantedAuthorities.add(grantedAuthority);
            });
        }
    // String username：用户名
    // String password： 密码
    // boolean enabled： 账号是否可用
    // boolean accountNonExpired：账号是否过期
    // boolean credentialsNonExpired：密码是否过期
    // boolean accountNonLocked：账号是否锁定
    // Collection<? extends GrantedAuthority> authorities)：用户权限列表
    return new User(
        sysUser.getAccount(),
        sysUser.getPassword(),
        sysUser.getEnabled(),
        sysUser.getAccountNonExpired(),
        sysUser.getCredentialsNonExpired(),
        sysUser.getAccountNonLocked(),
        grantedAuthorities);
    }
}
