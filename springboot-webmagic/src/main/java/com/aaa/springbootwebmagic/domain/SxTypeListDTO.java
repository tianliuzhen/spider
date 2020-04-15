package com.aaa.springbootwebmagic.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 生肖类型列表
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/15
 */
@Data
@Accessors(chain = true)
public class SxTypeListDTO {
    /**
     * 类型标题
     */
    private String  title;
    /**
     * 标题描述
     */
    private String  titleDesc;
    /**
     * 标题连接
     */
    private String  href;
    /**
     * 标题类型
     */
    private Integer  code;
}
