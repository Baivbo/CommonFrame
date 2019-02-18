package com.bwb.software.PubFrame.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.bwb.software.PubFrame.db.anno.TbField;
import com.bwb.software.PubFrame.db.anno.TbName;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Dao基类 包含数据真实操作
 * Created by baiweibo on 2018/11/14.
 */

public abstract class BaseDao<M> implements IBaseDao<M> {

    private String TAG = this.getClass().getSimpleName();
    private SQLiteDatabase mDatabase;
    private Class<M> mEntityClass;
    private String mTbName;
    private Map<String, Field> mFieldMap;

    protected boolean init(SQLiteDatabase mDatabase, Class<M> entity) {
        mDatabase = mDatabase;
        mEntityClass = entity;
        if (!mDatabase.isOpen()) {
            return false;
        }
        // 获取表名
        TbName tbName = entity.getAnnotation(TbName.class);
        mTbName = tbName == null ? entity.getSimpleName() : tbName.value();

        // 获取表映射字段
        if (!genFieldMap()) {
            return false;
        }

        // 创建数据库
        if (!createTable(mDatabase)) {
            return false;
        }

        return true;
    }

    /**
     * 创建表（可以被子类重写，方便灵活扩展）
     */
    protected boolean createTable(SQLiteDatabase database) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Field> entry : mFieldMap.entrySet()) {
            String columnName = entry.getKey();
            Field field = entry.getValue();
            TbField tbField = field.getAnnotation(TbField.class);
            int length = tbField == null ? 255 : tbField.lenght();
            String type = "";
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                type = "varchar";
            } else if (fieldType == int.class || fieldType == Integer.class) {
                type = "int";
            } else if (fieldType == double.class || fieldType == Double.class) {
                type = "double";
            } else if (fieldType == float.class || fieldType == Float.class) {
                type = "float";
            }
            if (TextUtils.isEmpty(type)) {
                Log.e(TAG, type.getClass().getName() + "是不支持的字段");
            } else {
                sb.append(columnName + " " + type + "(" + length + "),");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        String s = sb.toString();
        if (TextUtils.isEmpty(s)) {
            Log.e(TAG, "获取不到表字段信息");
            return false;
        }
        String sql = "create table if not exists " + mTbName + " (" + s + ") ";
        Log.e(TAG, sql);
        database.execSQL(sql);
        return true;
    }

    //将表面与字段名使用Map进行保存，便于后续各种操作
    private boolean genFieldMap() {
        mFieldMap = new HashMap<>();
        Field[] fields = mEntityClass.getFields();
        if (fields == null || fields.length == 0) {
            Log.e(TAG, "获取不到类中字段");
            return false;
        }
        for (Field field : fields) {
            field.setAccessible(true);
            TbField tbField = field.getAnnotation(TbField.class);
            mFieldMap.put(tbField == null ? field.getName() : tbField.value(), field);
        }
        return true;
    }

    /**
     * 增
     *
     * @param entity 新增需求数据类型为contentValues类型  所有先获取到Values，把键值对转换成contenValue执行
     * @return
     */
    @Override
    public Long insert(M entity) {
        try {
            Map<String, String> values = getValues(entity);
            ContentValues cv = getContentValues(values);
            return mDatabase.insert(mTbName, null, cv);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 改
     *
     * @param entity 新增需求数据类型为contentValues类型  所有先获取到Values，把键值对转换成contenValue
     * @param where  使用Condition类的构造函数对whereMap中value不为null的键值对取出来拼接。
     * @return
     */
    @Override
    public Integer update(M entity, M where) {
        try {
            Map<String, String> values = getValues(entity);
            ContentValues cv = getContentValues(values);

            Map<String, String> whereMap = getValues(where);
            Condition condition = new Condition(whereMap);

            return mDatabase.update(mTbName, cv, condition.whereClause, condition.whereArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查——将符合条件的表数据全部查询出来。
     *
     * @param where 所要查询的条件内容
     * @return
     */
    @Override
    public List<M> query(M where) {
        return query(where, null);
    }


    /**
     * 查——将符合条件的表数据查询出来，并可以排序
     *
     * @param where
     * @param orderBy
     * @return
     */
    @Override
    public List<M> query(M where, String orderBy) {
        return query(where, orderBy, null, null);
    }


    /**
     * 查——将符合条件的表数据查询出来，除了可以排序，还可以分页查询
     *
     * @param where
     * @param orderBy
     * @param page
     * @param pageCount
     * @return
     */
    @Override
    public List<M> query(M where, String orderBy, Integer page, Integer pageCount) {
        List<M> list = null;
        Cursor cursor = null;
        try {
            String limit = null;
            if (page != null && pageCount != null) {
                int startIndex = --page;
                limit = (startIndex < 0 ? 0 : startIndex) + "," + pageCount;
            }

            if (where != null) {
                Map<String, String> whereMap = getValues(where);
                Condition condition = new Condition(whereMap);
                cursor = mDatabase.query(mTbName, null, condition.whereClause, condition.whereArgs, null, null, orderBy, limit);
            } else {
                cursor = mDatabase.query(mTbName, null, null, null, null, null, orderBy, limit);
            }

            list = getDataList(cursor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return list;
    }

    /**
     * 删
     *
     * @param where 使用Condition类的构造函数对whereMap中value不为null的键值对取出来拼接。
     * @return
     */
    @Override
    public Integer delete(M where) {
        try {
            Map<String, String> whereMap = getValues(where);
            Condition condition = new Condition(whereMap);
            return mDatabase.delete(mTbName, condition.whereClause, condition.whereArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将对象中的属性转成键值对
     */
    private Map<String, String> getValues(M entity) throws IllegalAccessException {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Field> entry : mFieldMap.entrySet()) {
            Object value = entry.getValue().get(entity);
            result.put(entry.getKey(), value == null ? "" : value.toString());
        }
        return result;
    }

    /**
     * 将键值对转成ContentValues
     */
    private ContentValues getContentValues(Map<String, String> values) {
        ContentValues cv = new ContentValues();
        for (Map.Entry<String, String> val : values.entrySet()) {
            cv.put(val.getKey(), val.getValue());
        }
        return cv;
    }

    /**
     * 通过游标，将表中数据转成对象集合
     */
    private List<M> getDataList(Cursor cursor) throws IllegalAccessException, InstantiationException {
        if (cursor != null) {
            List<M> result = new ArrayList<>();
            // 遍历游标，获取表中一行行的数据
            while (cursor.moveToNext()) {

                // 创建对象
                ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();// 获取当前new的对象的 泛型的父类 类型
                Class<M> clazz = (Class<M>) pt.getActualTypeArguments()[0];// 获取第一个类型参数的真实类型
                M item = clazz.newInstance();

                // 遍历表字段，使用游标一个个取值，赋值给新创建的对象。
                Iterator<String> iterator = mFieldMap.keySet().iterator();
                while (iterator.hasNext()) {
                    // 找到表字段
                    String columnName = iterator.next();
                    // 找到表字段对应的类属性
                    Field field = mFieldMap.get(columnName);

                    // 根据类属性类型，使用游标获取表中的值
                    Object val = null;
                    Class<?> fieldType = field.getType();
                    if (fieldType == String.class) {
                        val = cursor.getString(cursor.getColumnIndex(columnName));
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        val = cursor.getInt(cursor.getColumnIndex(columnName));
                    } else if (fieldType == double.class || fieldType == Double.class) {
                        val = cursor.getDouble(cursor.getColumnIndex(columnName));
                    } else if (fieldType == float.class || fieldType == Float.class) {
                        val = cursor.getFloat(cursor.getColumnIndex(columnName));
                    }

                    // 反射给对象属性赋值
                    field.set(item, val);
                }
                // 将对象添加到集合中
                result.add(item);
            }
            return result;
        }
        return null;
    }

    class Condition {
        public Condition(Map<String, String> whereMap) {

            StringBuilder sb = new StringBuilder();
            List<String> list = new ArrayList<>();

            for (Map.Entry<String, String> entry : whereMap.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    sb.append("and " + entry.getKey() + "=? ");
                    list.add(entry.getValue());
                }
            }
            this.whereClause = sb.delete(0, 4).toString();
            this.whereArgs = list.toArray(new String[list.size()]);
        }

        String whereClause;
        String[] whereArgs;
    }
}
