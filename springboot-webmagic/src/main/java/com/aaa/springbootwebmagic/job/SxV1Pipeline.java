package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.domain.ArtTypeUtil;
import com.aaa.springbootwebmagic.domain.SxDTO;
import com.aaa.springbootwebmagic.domain.SxTypeListDTO;
import com.aaa.springbootwebmagic.domain.entity.*;
import com.aaa.springbootwebmagic.mapper.*;
import com.aaa.springbootwebmagic.util.MySimHash;
import com.aaa.springbootwebmagic.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/16
 */
@Component
public class SxV1Pipeline implements Pipeline {
    @Autowired
    private SxMain12Mapper sxMain12Mapper;
    @Autowired
    private SxTypeUtilMapper sxTypeUtilMapper;
    @Autowired
    private ArtTypeInfoMapper artTypeInfoMapper;
    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("get page: " + resultItems.getRequest().getUrl());
        if (resultItems.get("sx_12_main")!=null) {
            SxMain12 sxMain12 =   resultItems.get("sx_12_main");
            sxMain12Mapper.insert(sxMain12);
        }
        if (resultItems.get("sxDTOS2") != null) {
            List<SxDTO> sxDTOS = resultItems.get("sxDTOS2");
            for (SxDTO sxDTO : sxDTOS) {
                SxUtilType sxType = new SxUtilType();
                BeanUtils.copyProperties(sxDTO,sxType);
                sxType.setList(JSON.toJSONString(sxDTO.getList()));
                sxTypeUtilMapper.insert(sxType);

            }
        }
        if (resultItems.get("artTypeUtils") != null) {
            List<ArtTypeUtil> artTypeUtils = resultItems.get("artTypeUtils");
            for (ArtTypeUtil artTypeUtil : artTypeUtils) {
                artTypeUtil.setDetailHtml(StringUtil.getStringFilter(artTypeUtil.getDetailHtml()));
                ArtTypeInfo artTypeInfo = new ArtTypeInfo();
                BeanUtils.copyProperties(artTypeUtil, artTypeInfo);
                ArtTypeInfo artTypeInfo1 = artTypeInfoMapper.getArtTypeInfoByArtCode(artTypeUtil.getArtCode());
                if(artTypeInfo1!=null){
                    double semblance = CommonProcessor.simHash(artTypeUtil.getDetailHtml(), artTypeInfo1.getDetailHtml());
                    //如果相似度达到 0.9 以上不去更新文章
                    if(semblance<0.9){
                        ArtTypeInfo artTypeInfo2 = artTypeInfoMapper.selectById(artTypeInfo1.getId());
                        artTypeInfo2.setDetailHtml(artTypeUtil.getDetailHtml());
                        artTypeInfoMapper.updateById(artTypeInfo2);
                    }
                }else{
                    artTypeInfoMapper.insert(artTypeInfo);
                }

            }
        }
    }
}
