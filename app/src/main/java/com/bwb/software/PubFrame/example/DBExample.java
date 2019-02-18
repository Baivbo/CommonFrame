package com.bwb.software.PubFrame.example;

import android.util.Log;
import android.widget.Toast;

import com.bwb.software.PubFrame.base.BaseActivity;
import com.bwb.software.PubFrame.date.UserData;
import com.bwb.software.PubFrame.db.BaseDaoFactory;
import com.bwb.software.PubFrame.db.Dao.UserDao;

import java.io.File;
import java.util.List;

/**
 * Created by baiweibo on 2018/11/15.
 */

public class DBExample extends BaseActivity {

    private UserData userData;
    private UserDao userDao;


    @Override
    public void initView() {
        //建议写在自定义Application
        BaseDaoFactory.init(new File(getFilesDir(), "Frame_db.db").getAbsolutePath());
        Log.d("db文件路径", new File(getFilesDir(), "Frame_db.db").getAbsolutePath());
        userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, UserData.class);
        userData = new UserData("bai", "123465", 10);
    }

    @Override
    public void initDate() {

        //增
        Long insert = userDao.insert(userData);

        //删
        UserData where = new UserData();
        where.setName("bai");
        Integer delete = userDao.delete(where);

        //改
        UserData user = new UserData("LQR_CSDN", "654321", 9);

        UserData where1 = new UserData();
        where.setName("bai");

        Integer update = userDao.update(user, where1);

        //查1
        UserData whereq1 = new UserData();
        whereq1.setName("bai");

        List<UserData> list1 = userDao.query(whereq1);
        for (UserData user1 : list1) {
            System.out.println(user1);//第一种条件查询
        }

        //查2
        List<UserData> list2 = userDao.query(null, "tb_age asc");//asc正序   desc反序
        int query = list2 == null ? 0 : list2.size();
        Toast.makeText(getApplicationContext(), "查出了" + query + "条数据", Toast.LENGTH_SHORT).show();
        for (UserData user2 : list2) {
            System.out.println(user2);//第二种查询
        }

        //查3
        UserData where3 = new UserData();

        List<UserData> list = userDao.query(where, null, 1, 2);
        int query3 = list == null ? 0 : list.size();
        Toast.makeText(getApplicationContext(), "查出了" + query3 + "条数据", Toast.LENGTH_SHORT).show();
        for (UserData user3 : list) {
            System.out.println(user3);
        }
    }

    @Override
    protected int getLayout() {
        return 0;
    }
}
