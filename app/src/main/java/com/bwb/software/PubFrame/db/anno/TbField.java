package com.bwb.software.PubFrame.db.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表字段注解
 * Created by baiweibo on 2018/11/14.
 */

@Target(ElementType.FIELD)//TbField注解在类属性上
@Retention(RetentionPolicy.RUNTIME)//运行时解析
public @interface TbField {
    String value();//自定义字段名
    int lenght();//明确字段长度
}
