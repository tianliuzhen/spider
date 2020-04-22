package com.aaa.springbootwebmagic.mapper;

import com.aaa.springbootwebmagic.domain.entity.SxIndex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface SxIndexMapper extends BaseMapper<SxIndex> {
    @Select("select * from sx_index order By id desc limit 1 ")
    SxIndex getOne();
}
