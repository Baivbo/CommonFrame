package com.bwb.software.PubFrame.date;

import com.bwb.software.PubFrame.db.anno.TbField;
import com.bwb.software.PubFrame.db.anno.TbName;

/**
 * Created by baiweibo on 2018/11/14.
 */
@TbName("tb_user")
public class UserData {

    @TbField(value = "name",lenght = 30)
    public String name;
    @TbField(value = "password",lenght = 30)
    public String password;
    @TbField(value = "age",lenght = 11)
    public int age;

    public UserData(String name, String password, int age) {
        this.name = name;
        this.password = password;
        this.age = age;
    }

    public UserData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
