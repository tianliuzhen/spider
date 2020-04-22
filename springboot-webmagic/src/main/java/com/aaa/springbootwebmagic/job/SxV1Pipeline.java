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
    private CommonPipeline commonPipeline;
    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("get page: " + resultItems.getRequest().getUrl());
        if (resultItems.get("sx_12_main")!=null) {
            SxMain12 sxMain12 =   resultItems.get("sx_12_main");
            SxMain12 one = sxMain12Mapper.getOne(sxMain12.getCode());
            if(one!=null){
                double v = CommonProcessor.simHash(sxMain12.toString(), one.toString());
                if(v<1){
                    one.setImgUrl(sxMain12.getImgUrl()).setTitle(sxMain12.getTitle()).setTitleDesc(sxMain12.getTitleDesc()).setInfo(sxMain12.getInfo());
                    sxMain12Mapper.updateById(one);
                }
            }else {
                sxMain12Mapper.insert(sxMain12);
            }
        }
        if (resultItems.get("sxDTOS2") != null) {
            List<SxDTO> sxDTOS = resultItems.get("sxDTOS2");
            for (SxDTO sxDTO : sxDTOS) {
                SxUtilType sxType = new SxUtilType();
                BeanUtils.copyProperties(sxDTO,sxType);
                sxType.setList(JSON.toJSONString(sxDTO.getList()));
                SxUtilType one = sxTypeUtilMapper.getOne(sxType.getCode());
                if(one!=null){
                    double v = CommonProcessor.simHash(sxType.toString(), one.toString());
                    if (v<1){
                        BeanUtils.copyProperties(sxType,one,"id");
                        sxTypeUtilMapper.updateById(one);
                    }
                }else {
                    sxTypeUtilMapper.insert(sxType);
                }
            }
        }
        if (resultItems.get("artTypeUtils") != null) {
            List<ArtTypeUtil> artTypeUtils = resultItems.get("artTypeUtils");
            commonPipeline.editArtTypeUtils(artTypeUtils);
        }
    }
}
