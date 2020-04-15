package com.aaa.springbootwebmagic.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @Description  
 * @Author  idea
 * @Date 2020-04-15 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User  implements Serializable {

	private static final long serialVersionUID =  7403143422958949407L;

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 年龄
	 */
	private Long age;

	/**
	 * 邮箱
	 */
	private String email;
}
