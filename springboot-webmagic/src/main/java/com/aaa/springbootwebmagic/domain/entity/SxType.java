package com.aaa.springbootwebmagic.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author TLZ
 */
@Data
public class SxType {

  @TableId(value = "id",type = IdType.AUTO)
  private long id;

  private Integer type;
  private String code;
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

  @Override
  public String toString() {
    return "SxType{" +
            "type=" + type +
            ", code='" + code + '\'' +
            ", sxTypeName='" + sxTypeName + '\'' +
            ", sxTypeHref='" + sxTypeHref + '\'' +
            ", imgSrc1='" + imgSrc1 + '\'' +
            ", imgSrc2='" + imgSrc2 + '\'' +
            ", list='" + list + '\'' +
            ", imgTitle1='" + imgTitle1 + '\'' +
            ", imgTitle2='" + imgTitle2 + '\'' +
            '}';
  }
}
