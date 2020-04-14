package com.aaa.springbootwebmagic.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@Data
public class ItemDTO {
    private String title;
    private String titleHref;
    private List<ArtUtil> list;
}
