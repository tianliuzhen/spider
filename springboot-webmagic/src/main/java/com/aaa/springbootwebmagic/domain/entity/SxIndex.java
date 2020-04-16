package com.aaa.springbootwebmagic.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * description: 首页
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/16
 */
@Data
@TableName("sx_index")
public class SxIndex {
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    private String srcRoll;
}
