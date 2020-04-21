package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.domain.ArtTypeUtil;
import com.aaa.springbootwebmagic.domain.SxDTO;
import com.aaa.springbootwebmagic.domain.SxTypeListDTO;
import com.aaa.springbootwebmagic.domain.entity.*;
import com.aaa.springbootwebmagic.mapper.*;
import com.alibaba.fastjson.JSON;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/16
 */
@Component
public class SxPipeline implements Pipeline {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private SxIndexMapper sxIndexMapper;
    @Autowired
    private SxTypeMapper sxTypeMapper;
    @Autowired
    private SxTypeListMapper sxTypeListMapper;
    @Autowired
    private ArtTypeInfoMapper artTypeInfoMapper;
    @Autowired
    private SxMain12Mapper sxMain12Mapper;
    @Autowired
    private SxTypeUtilMapper sxTypeUtilMapper;
    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        if (resultItems.get("entity-sxindex") != null) {
            SxIndex sxIndex = new SxIndex();
            sxIndex.setSrcRoll(resultItems.get("entity-sxindex"));
            sxIndexMapper.insert(sxIndex);
        }
        if (resultItems.get("sxDTOS") != null) {
            List<SxDTO> sxDTOS = resultItems.get("sxDTOS");
            for (SxDTO sxDTO : sxDTOS) {
                SxType sxType = new SxType();
                BeanUtils.copyProperties(sxDTO,sxType);
                sxType.setList(JSON.toJSONString(sxDTO.getList()));
                sxTypeMapper.insert(sxType);

            }
        }
        if (resultItems.get("sxTypeListDTOS") != null) {
            List<SxTypeListDTO> sxTypeListDTOS = resultItems.get("sxTypeListDTOS");
            for (SxTypeListDTO sxTypeListDTO : sxTypeListDTOS) {
                SxTypeList sxTypeList = new SxTypeList();
                BeanUtils.copyProperties(sxTypeListDTO,sxTypeList);
                sxTypeListMapper.insert(sxTypeList);
            }

        }
        if (resultItems.get("artTypeUtils")!=null) {
            List<ArtTypeUtil> artTypeUtils = resultItems.get("artTypeUtils");
            for (ArtTypeUtil artTypeUtil : artTypeUtils) {
                ArtTypeInfo artTypeInfo = new ArtTypeInfo();
                BeanUtils.copyProperties(artTypeUtil,artTypeInfo);
                artTypeInfoMapper.insert(artTypeInfo);
            }
        }
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
    }
}
