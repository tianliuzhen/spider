package com.aaa.springbootwebmagic.domain;

import lombok.Data;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@Data
public class SxDTO {
    /**
     * 用于区别类型
     */
    private Integer code;
    private String sxTypeName;
    private String sxTypeHref;
    private List<SxUtil> list;

}
