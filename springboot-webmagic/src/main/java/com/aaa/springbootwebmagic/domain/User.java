package com.aaa.springbootwebmagic.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author  idea
 * @Date 2020-04-14
 */

@Data
public class User  implements Serializable {

	private static final long serialVersionUID =  651454540060616519L;

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
