package com.guangxuan.vo;

import lombok.Data;

/**
 * @author deofly
 * @since 2019-02-17
 */
@Data
public class SMSDetailResponse {

    String Code;

    String Message;

    String RequestId;

    Integer TotalCount;

    SMSDetails SmsSendDetailDTOs;
}
