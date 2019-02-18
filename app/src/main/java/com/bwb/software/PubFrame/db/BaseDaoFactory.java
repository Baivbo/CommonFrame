package com.bwb.software.PubFrame.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Dao类工厂，负责初始化打开数据库
 * Created by baiweibo on 2018/11/14.
 */

public class BaseDaoFactory {

    private static String mDbPath;
    private SQLiteDatabase mdatabase;

    public static class Instance{
        public static BaseDaoFactory INSTANCE =new BaseDaoFactory();
    }

    public static BaseDaoFactory getInstance(){
        return Instance.INSTANCE;
    }

    //初始化数据库存储位置
    public static void init(String dbPath){
        mDbPath=dbPath;
    }

    private BaseDaoFactory(){
        if(TextUtils.isEmpty(mDbPath)){
            throw new RuntimeException("在使用BaseDaoFactory之前，请调用BaseDaoFactory.init()初始化好数据库路径。");
        }
        mdatabase=SQLiteDatabase.openOrCreateDatabase(mDbPath,null);
    }

    //创建供外部获取需要的Dao类
    public <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entity) {
        T baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(mdatabase, entity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }
}
