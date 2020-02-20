package com.guangxuan.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FlywaySchemaHistory对象", description="")
public class FlywaySchemaHistory extends Model<FlywaySchemaHistory> {

    private static final long serialVersionUID=1L;

    private Integer installedRank;

    private String version;

    private String description;

    private String type;

    private String script;

    private Integer checksum;

    private String installedBy;

    private LocalDateTime installedOn;

    private Integer executionTime;

    private Boolean success;


    @Override
    protected Serializable pkVal() {
        return this.installedRank;
    }

}
