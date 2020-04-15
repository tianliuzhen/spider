package com.aaa.springbootwebmagic.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 生肖首页
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/15
 */
@Data
@Accessors(chain = true)
public class SxIndex {
    /**
     * 主图
     */
    private String mainUrl;

    /**
     * 轮播图
     */
    private String srcUrl1;
    private String srcTitle1;
    private String srcUrl2;
    private String srcTitle2;
    private String srcUrl3;
    private String srcTitle3;
}
