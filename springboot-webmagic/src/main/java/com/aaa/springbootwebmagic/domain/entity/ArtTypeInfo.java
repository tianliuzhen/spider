package com.aaa.springbootwebmagic.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@Data
@Accessors(chain = true)
@TableName("sx_art_type_info")
public class ArtTypeInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String href;
    private String title;
    private String detailHtml;

    /**
     * 生肖文章类型ID
     */
    private String sxTypeCode;
    /**
     * 生肖类型ID
     */
    private String artCode;

}
