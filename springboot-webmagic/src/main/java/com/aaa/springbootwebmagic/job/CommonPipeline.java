package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.domain.ArtTypeUtil;
import com.aaa.springbootwebmagic.domain.entity.ArtTypeInfo;
import com.aaa.springbootwebmagic.mapper.ArtTypeInfoMapper;
import com.aaa.springbootwebmagic.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/22
 */
@Component
public class CommonPipeline {

    @Autowired
    private ArtTypeInfoMapper artTypeInfoMapper;

    public void editArtTypeUtils( List<ArtTypeUtil> artTypeUtils) {
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
