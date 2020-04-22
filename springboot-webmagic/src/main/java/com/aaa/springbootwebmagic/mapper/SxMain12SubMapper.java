package com.aaa.springbootwebmagic.mapper;

import com.aaa.springbootwebmagic.domain.entity.SxMain12;
import com.aaa.springbootwebmagic.domain.entity.SxMain12Sub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/17
 */
@Repository
public interface SxMain12SubMapper extends BaseMapper<SxMain12Sub> {
    @Select(" select * from sx_main_12_sub where code =#{code} order by id desc limit 1 ")
    SxMain12Sub getOne(@Param("code") String code);
}
