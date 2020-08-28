package com.aaa.springbootwebmagic.mapper;

import com.aaa.springbootwebmagic.domain.entity.SxTypeList;
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
public interface SxTypeListMapper extends BaseMapper<SxTypeList> {
    @Select(" SELECT * FROM `sx_type_list` WHERE `sx_type_code`=#{sxTypeCode} AND `art_code`=#{artCode} order by id desc limit 1 ")
    SxTypeList getOne(@Param("sxTypeCode") String sxTypeCode,@Param("artCode") String artCode);
}
