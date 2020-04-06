package aaa.webmagic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JDGoodsInfo {
    private Double price ;
    private String goodsTitle;
    private String goodUtilUrl;
    private String goodsImg;
}
