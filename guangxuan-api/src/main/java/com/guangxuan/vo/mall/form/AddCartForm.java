package com.guangxuan.vo.mall.form;

import lombok.Data;

import java.io.Serializable;

/**
 * 加购表单
 *
 * @author deofly
 * @since 2019-05-13
 */
@Data
public class AddCartForm implements Serializable {

    private static final long serialVersionUID = 6877848260434947831L;

    private int item;

    private int quantity;
}
