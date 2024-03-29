package com.aaa.springbootwebmagic.mapper;

import com.aaa.springbootwebmagic.domain.entity.SxType;
import com.aaa.springbootwebmagic.domain.entity.SxUtilType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/16
 */
@Repository
public interface SxTypeUtilMapper extends BaseMapper<SxUtilType> {
    @Select(" SELECT * FROM `sx_util_type` WHERE `sx_type_href` = #{code} AND `sx_type_name` = #{name} limit 1")
    SxUtilType getOne(@Param("code") String code,@Param("name") String name);
}
