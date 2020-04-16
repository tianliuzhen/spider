package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.domain.SxDTO;
import com.aaa.springbootwebmagic.domain.entity.SxIndex;
import com.aaa.springbootwebmagic.domain.entity.SxType;
import com.aaa.springbootwebmagic.mapper.SxIndexMapper;
import com.aaa.springbootwebmagic.mapper.SxTypeMapper;
import com.aaa.springbootwebmagic.mapper.UserMapper;
import com.alibaba.fastjson.JSON;
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

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        if(resultItems.get("entity-sxindex")!=null){
            SxIndex sxIndex =new SxIndex() ;
            sxIndex.setSrcRoll(resultItems.get("entity-sxindex"));
            sxIndexMapper.insert(sxIndex);
        }
        if(resultItems.get("sxDTOS")!=null){
            List<SxDTO> sxDTOS= resultItems.get("sxDTOS");
            for (SxDTO sxDTO : sxDTOS) {
                SxType sxType=new SxType();
                sxType.setCode(sxDTO.getCode());
                sxType.setImgSrc1(sxDTO.getImgSrc1());
                sxType.setImgSrc2(sxDTO.getImgSrc2());
                sxType.setSxTypeName(sxDTO.getSxTypeName());
                sxType.setSxTypeHref(sxDTO.getSxTypeHref());
                sxType.setList(JSON.toJSONString(sxDTO.getList()));
                sxTypeMapper.insert(sxType);
            }


        }

    }
}
