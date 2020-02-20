package com.guangxuan.enumration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhuolin
 * @Date 2019/12/23
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
    /**
     * 系统消息
     */
    SYSTEM_MESSAGE(1, "系统消息"),
    ;

    Integer type;

    String typeName;

    public static MessageTypeEnum valueOfType(Integer type){
        if(type == null){
            return null;
        }
        for(MessageTypeEnum messageTypeEnum: MessageTypeEnum.values()){
            if(messageTypeEnum.getType().equals(type)){
                return messageTypeEnum;
            }
        }
        return null;
    }
}
