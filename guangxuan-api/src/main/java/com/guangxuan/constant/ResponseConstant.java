package com.guangxuan.constant;

import com.guangxuan.vo.BaseResponse;
import com.guangxuan.vo.ErrorResponse;

/**
 * @author deofly
 * @since 2019-04-26
 */
public final class ResponseConstant {

    public static BaseResponse SESSION_EXPIRED = new ErrorResponse("登录会话已过期");
}
