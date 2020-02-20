package com.guangxuan.vo;

import lombok.Data;

/**
 * @author deofly
 * @since 2019-02-17
 */
@Data
public class SMSDetail {

    /**
     * 短信内容
     */
    String Content;

    /**
     * 运营商短信状态码。
     *
     * 短信发送成功：DELIVRD
     */
    String ErrCode;

    /**
     * 外部流水扩展字段
     */
    String OutId;

    /**
     * 接收短信的手机号码
     */
    String PhoneNum;

    /**
     * 短信接收日期和时间
     *
     * e.g. 2019-01-08 16:44:13
     */
    String ReceiveDate;

    String SendDate;

    Integer SendStatus;

    String TemplateCode;
}
