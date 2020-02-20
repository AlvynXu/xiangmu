package com.guangxuan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/11/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromoterDTO implements Serializable {

    private Long userId;

    private List<PromoterDTO> promoterDTOS;
}
