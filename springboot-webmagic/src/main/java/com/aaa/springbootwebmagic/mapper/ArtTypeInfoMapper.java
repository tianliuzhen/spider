package com.aaa.springbootwebmagic.mapper;

import com.aaa.springbootwebmagic.domain.entity.ArtTypeInfo;
import com.aaa.springbootwebmagic.domain.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public interface ArtTypeInfoMapper extends BaseMapper<ArtTypeInfo> {

    @Select({" select * from sx_art_type_info where art_code =#{code} order by id desc limit 1 "})
    ArtTypeInfo getArtTypeInfoByArtCode(@Param("code") String code);
}
