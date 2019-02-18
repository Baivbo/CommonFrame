package com.bwb.software.PubFrame.db;

import java.util.List;

/**
 * 基本数据操作
 * Created by baiweibo on 2018/11/14.
 */

public interface IBaseDao<M> {

     Long insert(M entity);

    Integer delete(M entity);

    Integer update(M data,M where);

    List<M> query(M where);

    List<M> query(M where,String orderBy);

    List<M> query(M where,String orderBy,Integer page,Integer pageCount);
}
