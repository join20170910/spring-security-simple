package com.ws.security.common.utils;


import com.ws.security.common.entity.JsonResult;
import com.ws.security.common.enums.ResultCode;


/**
 * @Description:    //TODO 返回体构造工具
 * @Author:         john
 * @CreateDate:     2020/3/8 11:58
 */
public class ResultTool {
    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(ResultCode resultEnum) {
        return new JsonResult(false, resultEnum);
    }
}
