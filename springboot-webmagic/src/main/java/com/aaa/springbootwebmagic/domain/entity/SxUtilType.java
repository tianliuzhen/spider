package com.aaa.springbootwebmagic.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author TLZ
 */
@Data
public class SxUtilType {

  @TableId(value = "id",type = IdType.AUTO)
  private long id;

  private Integer type;
  private Integer code;
  private String sxTypeName;
  private String sxTypeHref;
  private String imgSrc1;
  private String imgSrc2;
  private String list;
  /**
   * imgTitle
   */
  private String imgTitle1 ;
  private String imgTitle2 ;


}
