package com.bwb.software.PubFrame.db.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名注解
 * Created by baiweibo on 2018/11/14.
 */

@Target(ElementType.TYPE)//TbName需要注解在类上的
@Retention(RetentionPolicy.RUNTIME)//因解析处于运行时所以使用RUNTIME
public @interface TbName {
    String value();//自定义表名
}
