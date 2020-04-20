package com.aaa.springbootwebmagic.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description: 生肖四个大的类型
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@Data
@Accessors(chain = true)
public class SxDTO {
    /**
     * 用于区别类型
     */
    private Integer code;
    private String sxTypeName;
    private String sxTypeHref;
    /**
     * 标题附带的列表详情
     */
    private List<SxUtil> list;
    /**
     * imgSrc
     */
    private String imgSrc1 ;
    private String imgSrc2 ;
    /**
     * imgTitle
     */
    private String imgTitle1 ;
    private String imgTitle2 ;
}
