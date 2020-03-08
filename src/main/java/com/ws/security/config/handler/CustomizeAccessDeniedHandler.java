package com.ws.security.config.handler;

import com.alibaba.fastjson.JSON;
import com.ws.security.common.entity.JsonResult;
import com.ws.security.common.enums.ResultCode;
import com.ws.security.common.utils.ResultTool;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Description:    //TODO 权限拒绝处理逻辑
 * @Author:         john
 * @CreateDate:     2020/3/8 12:30
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 12:30
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonResult result = ResultTool.fail(ResultCode.NO_PERMISSION);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
