package com.aaa.springbootwebmagic.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @Description
 * @Author  idea
 * @Date 2020-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sx_main_12")
public class SxMain12  implements Serializable {

	private static final long serialVersionUID =  3313252423267407787L;

	@TableId(value = "id",type = IdType.AUTO)
	private long id;

	private String code;

	private String codeId;

	/**
	 * 图片url
	 */
	private String imgUrl;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 详情
	 */
	private String titleDesc;

	/**
	 * 文本
	 */
	private String info;

	@Override
	public String toString() {
		return "SxMain12{" +
				"code='" + code + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", title='" + title + '\'' +
				", titleDesc='" + titleDesc + '\'' +
				", info='" + info + '\'' +
				'}';
	}
}
