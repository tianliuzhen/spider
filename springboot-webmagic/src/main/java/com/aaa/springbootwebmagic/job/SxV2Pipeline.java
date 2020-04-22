package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.domain.ArtTypeUtil;
import com.aaa.springbootwebmagic.domain.SxDTO;
import com.aaa.springbootwebmagic.domain.SxTypeListDTO;
import com.aaa.springbootwebmagic.domain.entity.*;
import com.aaa.springbootwebmagic.mapper.*;
import com.alibaba.fastjson.JSON;
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
public class SxV2Pipeline implements Pipeline {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private SxIndexMapper sxIndexMapper;
    @Autowired
    private SxTypeMapper sxTypeMapper;
    @Autowired
    private SxTypeListMapper sxTypeListMapper;
    @Autowired
    private CommonPipeline commonPipeline;
    @Autowired
    private SxMain12SubMapper sxMain12SubMapper;
    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        if (resultItems.get("entity-sxindex") != null) {
            SxIndex sxIndex = new SxIndex();
            sxIndex.setSrcRoll(resultItems.get("entity-sxindex"));
            SxIndex one = sxIndexMapper.getOne();
            if(one!=null){
                double v = CommonProcessor.simHash(sxIndex.getSrcRoll(), one.getSrcRoll());
                if(v<1){
                    one.setSrcRoll(sxIndex.getSrcRoll());
                    sxIndexMapper.updateById(one);
                }
            }else {
                sxIndexMapper.insert(sxIndex);
            }
        }
        if (resultItems.get("sxDTOS") != null) {
            List<SxDTO> sxDTOS = resultItems.get("sxDTOS");
            for (SxDTO sxDTO : sxDTOS) {
                SxType sxType = new SxType();
                BeanUtils.copyProperties(sxDTO, sxType);
                sxType.setList(JSON.toJSONString(sxDTO.getList()));
                SxType one = sxTypeMapper.getOne(sxDTO.getCode());
                if(one!=null){
                    double v = CommonProcessor.simHash(one.toString(), sxType.toString());
                    if(v<1){
                        BeanUtils.copyProperties(sxType, one,"id");
                        sxTypeMapper.updateById(one);
                    }
                }else {
                    sxTypeMapper.insert(sxType);
                }
            }
        }
        if (resultItems.get("sxTypeListDTOS") != null) {
            List<SxTypeListDTO> sxTypeListDTOS = resultItems.get("sxTypeListDTOS");
            for (SxTypeListDTO sxTypeListDTO : sxTypeListDTOS) {
                SxTypeList sxTypeList = new SxTypeList();
                BeanUtils.copyProperties(sxTypeListDTO, sxTypeList);

                SxTypeList one = sxTypeListMapper.getOne(sxTypeList.getSxTypeCode(), sxTypeList.getArtCode());
                if (one!=null) {
                    double v = CommonProcessor.simHash(one.toString(), sxTypeList.toString());
                    if (v<1){
                        BeanUtils.copyProperties(sxTypeList, one,"id");
                        sxTypeListMapper.updateById(one);
                    }
                }else {
                    sxTypeListMapper.insert(sxTypeList);
                }
            }

        }
        if (resultItems.get("artTypeUtils") != null) {
            List<ArtTypeUtil> artTypeUtils = resultItems.get("artTypeUtils");
            commonPipeline.editArtTypeUtils(artTypeUtils);
        }
        if (resultItems.get("SxMain12Sub") != null) {
            SxMain12Sub sxMain12Sub = resultItems.get("SxMain12Sub");
            SxMain12Sub one = sxMain12SubMapper.getOne(sxMain12Sub.getCode());
            if(one!=null){
                double v = CommonProcessor.simHash(sxMain12Sub.toString(), one.toString());
                if(v<1){
                    BeanUtils.copyProperties(sxMain12Sub, one,"id");
                    sxMain12SubMapper.updateById(one);
                }
            }else {
                sxMain12SubMapper.insert(sxMain12Sub);
            }
        }

    }


}
