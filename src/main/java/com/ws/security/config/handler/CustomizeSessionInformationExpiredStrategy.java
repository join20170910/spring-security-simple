package com.ws.security.config.handler;

import com.alibaba.fastjson.JSON;
import com.ws.security.common.entity.JsonResult;
import com.ws.security.common.enums.ResultCode;
import com.ws.security.common.utils.ResultTool;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Description:    //TODO 会话信息过期策略
 * @Author:         john
 * @CreateDate:     2020/3/8 12:33
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 12:33
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
@Component
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        JsonResult result = ResultTool.fail(ResultCode.USER_ACCOUNT_USE_BY_OTHERS);
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
