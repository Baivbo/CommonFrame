package com.bwb.software.PubFrame.net;

import com.bwb.software.PubFrame.date.MovieEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by baiweibo on 2018/11/7.
 */

public interface NetAPi {
    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start , @Query("count") int count);
}
