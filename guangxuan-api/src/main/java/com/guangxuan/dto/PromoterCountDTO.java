package com.guangxuan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/11/26
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoterCountDTO implements Serializable {

    private String time;

    private String name;

    private Integer indirectCount;

    private Integer totalCount;

    private Integer directCount;

    List<PromoterCountDTO> promoterCountDTOList;


}