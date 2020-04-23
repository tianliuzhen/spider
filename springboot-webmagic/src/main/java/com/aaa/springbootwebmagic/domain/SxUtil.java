package com.aaa.springbootwebmagic.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@Data
@Accessors(chain = true)
public class SxUtil {
    private String sxArtTitle;
    private String sxArtHref;
    private String sxArtUtil;
}
