package com.guangxuan.vo.admin.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author deofly
 * @since 2019-05-24
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MallOrderFilter implements Serializable {

    private static final long serialVersionUID = 2056334052431248541L;

    private Integer status;

    private String phone;

    private String orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
