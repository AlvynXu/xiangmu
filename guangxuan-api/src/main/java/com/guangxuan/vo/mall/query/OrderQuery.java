package com.guangxuan.vo.mall.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author deofly
 * @since 2019-05-03
 */
@Data
public class OrderQuery implements Serializable {

    private static final long serialVersionUID = 2878394172233654008L;

    @NotNull
    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    private Integer size;

    private Integer status;
}
